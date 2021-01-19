package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.dao.mapper.GiftCertificateMapper;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final static Logger logger = Logger.getLogger(GiftCertificateDaoImpl.class);
    private final static String PERCENT = "%";
    private final static String READ_GIFT_CERTIFICATE_SQL =
            "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE id=?";
    private final static String CREATE_GIFT_CERTIFICATE_SQL =
            "INSERT INTO gift_certificate(name,description,price,duration,create_date,last_update_date)" +
                    " VALUES (?,?,?,?,?,?)";
    private final static String DELETE_GIFT_CERTIFICATE_SQL = "DELETE FROM gift_certificate WHERE ID=?";
    private final static String SELECT_GIFT_CERTIFICATE_BY_TAG_SQL =
            "SELECT c.id,c.name,c.description,c.price,c.duration,c.create_date,c.last_update_date " +
                    "from gift_certificate c inner join tag_certificate tc on c.id=tc.id_certificate " +
                    "inner join tag t on tc.id_tag=t.id WHERE t.name=?";
    private final static String SELECT_BY_NAME_OR_DESC = "SELECT id,name,description,price,duration,create_date," +
            "last_update_date FROM gift_certificate WHERE name OR description LIKE ?";
    private final static String SORT_BY_NAME_SQL =
            "SELECT id,name,description,price,duration,create_date,last_update_date" +
                    " FROM gift_certificate ORDER BY name ";
    private final static String SORT_BY_DATE_SQL =
            "SELECT id,name,description,price,duration,create_date,last_update_date " +
                    "FROM gift_certificate ORDER BY create_date ";
    private final static String UPDATE_FIRST_PART_SQL = "UPDATE gift_certificate SET ";
    private final static String UPDATE_SECOND_PART_SQL = " WHERE id=?";

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
    public List<GiftCertificate> findByTag(String tag) throws DaoException {
        try {
            List<GiftCertificate> certificates =
                    jdbcTemplate.query(SELECT_GIFT_CERTIFICATE_BY_TAG_SQL, new Object[]{tag},
                            new GiftCertificateMapper());
            return certificates;
        } catch (DataAccessException e) {
            logger.error("Find by tag exception", e);
            throw new DaoException("Find by tag exception", e);
        }
    }

    @Override
    public List<GiftCertificate> searchByNameOrDesc(String part) throws DaoException {
        try {
            String sqlPart = PERCENT + part + PERCENT;
            List<GiftCertificate> certificates =
                    jdbcTemplate.query(SELECT_BY_NAME_OR_DESC, new Object[]{sqlPart}, new GiftCertificateMapper());
            return certificates;
        } catch (DataAccessException e) {
            logger.error("Search by name or description exception", e);
            throw new DaoException("Search by name or description exception", e);
        }
    }

    @Override
    public List<GiftCertificate> sortByDate(SortType sortType) throws DaoException {
        try {
            StringBuilder builder = new StringBuilder(SORT_BY_DATE_SQL);
            if (sortType == SortType.DESC) {
                builder.append(SortType.DESC);
            }
            List<GiftCertificate> certificates =
                    jdbcTemplate.query(builder.toString(), new GiftCertificateMapper());
            return certificates;
        } catch (DataAccessException e) {
            logger.error("Sort by date exception", e);
            throw new DaoException("Sort by date exception", e);
        }
    }

    @Override
    public List<GiftCertificate> sortByName(SortType sortType) throws DaoException {
        try {
            StringBuilder builder = new StringBuilder(SORT_BY_NAME_SQL);
            if (sortType == SortType.DESC) {
                builder.append(SortType.DESC);
            }
            List<GiftCertificate> certificates =
                    jdbcTemplate.query(builder.toString(), new GiftCertificateMapper());
            return certificates;
        } catch (DataAccessException e) {
            logger.error("Sort by name exception", e);
            throw new DaoException("Sort by name exception", e);
        }
    }
}
