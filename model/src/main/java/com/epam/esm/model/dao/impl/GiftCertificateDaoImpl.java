package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.dao.mapper.GiftCertificateMapper;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final static Logger logger = Logger.getLogger(GiftCertificateDaoImpl.class);
    private final static String PERCENT = "%";
    private final static String READ_GIFT_CERTIFICATE_SQL =
            "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE id=?";
    private final static String DELETE_GIFT_CERTIFICATE_SQL = "DELETE FROM gift_certificate WHERE ID=?";
    private final static String UPDATE_FIRST_PART_SQL = "UPDATE gift_certificate SET ";
    private final static String UPDATE_SECOND_PART_SQL = " WHERE id=?";
    private final static String FILTER_BY_PARAMETER_SQL =
            "SELECT c.id,c.name,c.description,c.price,c.duration,c.create_date,c.last_update_date " +
                    "from gift_certificate c inner join tag_certificate tc on c.id=tc.id_certificate " +
                    "inner join tag t on tc.id_tag=t.id ";


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("gift_certificate")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public GiftCertificate create(GiftCertificate certificate) throws DaoException {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("name", certificate.getName());
            parameters.put("description", certificate.getDescription());
            parameters.put("price", certificate.getPrice());
            parameters.put("duration", certificate.getDuration());
            parameters.put("create_date", certificate.getCreateDate());
            parameters.put("last_update_date", certificate.getLastUpdateDate());
            Number id = jdbcInsert.executeAndReturnKey(parameters);
            certificate.setId(id.longValue());
            return certificate;
        } catch (DataAccessException e) {
            logger.error("Create certificate exception", e);
            throw new DaoException("Create certificate exception", e);
        }
    }

    @Override
    public GiftCertificate read(Long id) throws DaoException {
        try {
            GiftCertificate certificate = jdbcTemplate
                    .queryForObject(READ_GIFT_CERTIFICATE_SQL, new Object[]{id}, new GiftCertificateMapper());
            return certificate;
        } catch (DataAccessException e) {
            logger.error("Read certificate exception", e);
            throw new DaoException("Read certificate exception", e);
        }
    }

    @Override
    public long update(CertificateDTO certificate) throws DaoException {
        try {
            Map<String, Object> data = new ObjectMapper().convertValue(certificate, Map.class);
            Map<String, Object> map = data.entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .filter(e -> !e.getKey().equals("id"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Long id = (long) data.get("id");
            StringBuilder builder = new StringBuilder(UPDATE_FIRST_PART_SQL);
            int size = map.size();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.equals("createDate") || key.equals("lastUpdateDate")) {
                    if (key.equals("createDate")) {
                        builder.append("create_date");
                    } else {
                        builder.append("last_update_date");
                    }
                    builder.append("=").append("'");
                    LocalDateTime dateTime = LocalDateTime.parse(value.toString());
                    builder.append(dateTime).append("'");
                } else {
                    builder.append(key).append("=").append("'").append(value.toString()).append("'");
                }
                size--;
                if (size > 0) {
                    builder.append(",");
                }
            }
            builder.append(UPDATE_SECOND_PART_SQL);
            int rows = jdbcTemplate.update(builder.toString(), id);
            return rows > 0L ? id : -1L;
        } catch (DataAccessException e) {
            logger.error("Update certificate exception", e);
            throw new DaoException("Update certificate exception", e);
        }
    }

    @Override
    public long delete(Long id) throws DaoException {
        try {
            int rows = jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_SQL, id);
            return rows > 0L ? id : -1L;
        } catch (DataAccessException e) {
            logger.error("Delete certificate exception", e);
            throw new DaoException("Delete certificate exception", e);
        }
    }

    @Override
    public List<GiftCertificate> filterByParameters(String tag, String part, String sortBy, SortType type) throws DaoException {
        try {
            String sql = buildSelectSQL(tag, part, sortBy, type);
            String likePartExpression = isNotEmpty(part) ? PERCENT + part + PERCENT : part;
            Object[] objects = Stream.of(tag, likePartExpression)
                    .filter(StringUtils::isNotEmpty)
                    .toArray();
            List<GiftCertificate> certificates = jdbcTemplate.query(sql, objects, new GiftCertificateMapper());
            return certificates;
        } catch (DataAccessException e) {
            logger.error("Filter by parameter exception", e);
            throw new DaoException("Filter by parameter exception", e);
        }
    }

    private String buildSelectSQL(String tag, String part, String sortBy, SortType type) {
        StringBuilder builder = new StringBuilder(FILTER_BY_PARAMETER_SQL);
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
        builder.append("GROUP BY c.id ");
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
}
