package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.dao.mapper.GiftCertificateExtractor;
import com.epam.esm.model.dao.mapper.TagMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final static Logger logger = Logger.getLogger(GiftCertificateDaoImpl.class);
    private final static String PERCENT = "%";
    private final static String DELETE_GIFT_CERTIFICATE_SQL = "DELETE FROM gift_certificate WHERE ID=?";
    private final static String SELECT_CERTIFICATE_TAGS_SQL = "SELECT id, name FROM certificates.tag c " +
            "inner join tag_certificate t on c.id=t.id_tag Where t.id_certificate=?";
    private final static String BASIC_SELECT_SQL =
            "SELECT c.id,c.name,c.description,c.price,c.duration,c.create_date,c.last_update_date,t.id,t.name " +
                    "from gift_certificate c left join tag_certificate tc on c.id=tc.id_certificate " +
                    "left join tag t on tc.id_tag=t.id ";
    private final static String TAG_CERTIFICATE_INSERT_SQL =
            "INSERT INTO tag_certificate (id_tag, id_certificate) VALUES (?, ?)";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert certificateInsert;
    private final TagDao tagDao;

    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, TagDao tagDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.certificateInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("gift_certificate")
                .usingGeneratedKeyColumns("id");
        this.tagDao = tagDao;
    }

    @Override
    public Optional<GiftCertificate> create(GiftCertificate certificate) throws DaoException {
        try {
            Number certificateId = certificateInsert.executeAndReturnKey(toMapParameters(certificate));
            certificate.setId(certificateId.longValue());
            List<Tag> tags = certificate.getTags();
            if (tags != null) {
                tags.forEach(tag -> {
                    if (tag.getId() == null) {
                        Optional<Tag> tagDB = Optional.empty();
                        try {
                            tagDB = tagDao.create(tag);
                        } catch (DaoException e) {
                            logger.error("Tag create exception with");
                        }
                        tagDB.ifPresent(t -> tag.setId(t.getId()));
                    }
                    if (tag.getId() != null) {
                        jdbcTemplate.update(TAG_CERTIFICATE_INSERT_SQL, tag.getId(), certificate.getId());
                    }
                });
            }
            return Optional.of(certificate);
        } catch (DataAccessException e) {
            logger.error("Create certificate exception", e);
            throw new DaoException("Create certificate exception", e);
        }
    }

    @Override
    public Optional<GiftCertificate> read(Long id) throws DaoException {
        try {
            String sql = BASIC_SELECT_SQL + "WHERE c.id=?";
            List<GiftCertificate> certificates = jdbcTemplate
                    .query(sql, new Object[]{id}, new GiftCertificateExtractor());
            if (certificates == null) {
                return Optional.empty();
            }
            return certificates.stream().filter(c -> c.getId().equals(id)).findFirst();
        } catch (DataAccessException e) {
            logger.error("Read certificate exception", e);
            throw new DaoException("Read certificate exception", e);
        }
    }

    @Override
    public void update(GiftCertificate certificate) throws DaoException {
        try {
            Map<String, Object> parameters = toMapParameters(certificate);
            Map<String, Object> updateParameters = parameters.entrySet().stream()
                    .filter(p -> p.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Long id = certificate.getId();
            updateCertificateWithoutTag(updateParameters, id);
            List<Tag> tags = certificate.getTags();
            if (tags != null) {
                List<Tag> dbTags = jdbcTemplate.query(SELECT_CERTIFICATE_TAGS_SQL, new Object[]{id}, new TagMapper());
                if (isNotEquals(tags, dbTags)) {
                    tagDao.updateNewTagByCertificate(tags, dbTags, id);
                    tagDao.updateOldTagByCertificate(tags, dbTags, id);
                }
            }
        } catch (DataAccessException e) {
            logger.error("Update certificate exception", e);
            throw new DaoException("Update certificate exception", e);
        }
    }

    @Override
    public Optional<Long> delete(Long id) throws DaoException {
        try {
            int rows = jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_SQL, id);
            return rows > 0L ? Optional.of(id) : Optional.empty();
        } catch (DataAccessException e) {
            logger.error("Delete certificate exception", e);
            throw new DaoException("Delete certificate exception", e);
        }
    }

    @Override
    public Optional<List<GiftCertificate>> filterByParameters(String tag, String part, String sortBy, SortType type) throws DaoException {
        try {
            String sql = buildFilterSQL(tag, part, sortBy, type);
            String likePartExpression = isNotEmpty(part) ? PERCENT + part + PERCENT : part;
            Object[] objects = Stream.of(tag, likePartExpression)
                    .filter(StringUtils::isNotEmpty)
                    .toArray();
            List<GiftCertificate> certificates = jdbcTemplate.query(sql, objects, new GiftCertificateExtractor());
            return Optional.ofNullable(certificates);
        } catch (DataAccessException e) {
            logger.error("Filter by parameter exception", e);
            throw new DaoException("Filter by parameter exception", e);
        }
    }

    private String buildFilterSQL(String tag, String part, String sortBy, SortType type) {
        StringBuilder builder = new StringBuilder(BASIC_SELECT_SQL);
        if (isNotEmpty(tag) || isNotEmpty(part)) {
            builder.append("WHERE ");
            if (isNotEmpty(tag) && isNotEmpty(part)) {
                builder.append("t.name=? AND (c.name OR c.description LIKE ?) ");
            } else if (isNotEmpty(tag)) {
                builder.append("t.name=? ");
            } else {
                builder.append("c.name OR c.description LIKE ? ");
            }
        }
        if (isNotEmpty(sortBy) || type != null) {
            builder.append("ORDER BY ");
            if (isNotEmpty(sortBy) && sortBy.equalsIgnoreCase("date")) {
                builder.append("c.create_date ");
            } else {
                builder.append("c.name ");
            }
            if (type != null) {
                builder.append(type);
            }
        }
        return builder.toString();
    }

    private String buildUpdateSQL(Set<String> keySet) {
        StringBuilder builder = new StringBuilder("UPDATE gift_certificate SET ");
        int size = keySet.size();
        for (String key : keySet) {
            builder.append(key).append("=?");
            size--;
            if (size > 0) {
                builder.append(", ");
            }
        }
        builder.append(" WHERE id=?");
        return builder.toString();
    }

    private Map<String, Object> toMapParameters(GiftCertificate certificate) {
        Map<String, Object> certificateParam = new HashMap<>();
        certificateParam.put("name", certificate.getName());
        certificateParam.put("description", certificate.getDescription());
        certificateParam.put("price", certificate.getPrice());
        certificateParam.put("duration", certificate.getDuration());
        certificateParam.put("create_date", certificate.getCreateDate());
        certificateParam.put("last_update_date", certificate.getLastUpdateDate());
        return certificateParam;
    }

    private void updateCertificateWithoutTag(Map<String, Object> updateParameters, Long id) {
        if (updateParameters.size() > 0) {
            String updateCertificate = buildUpdateSQL(updateParameters.keySet());
            List<Object> values = new ArrayList<>(updateParameters.values());
            values.add(id);
            jdbcTemplate.update(updateCertificate, values.toArray());
        }
    }

    private boolean isNotEquals(List<Tag> tags, List<Tag> dbTags) {
        if (tags.size() != dbTags.size()) {
            return true;
        }
        Collections.sort(tags);
        Collections.sort(dbTags);
        return tags.equals(dbTags);
    }
}
