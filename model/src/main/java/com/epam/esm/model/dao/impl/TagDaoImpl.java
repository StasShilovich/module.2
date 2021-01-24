package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.dao.mapper.TagMapper;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TagDaoImpl implements TagDao {

    private final static Logger logger = Logger.getLogger(TagDaoImpl.class);
    private final static String READ_TAG_SQL = "SELECT id,name FROM tag WHERE id=?";
    private final static String DELETE_TAG_SQL = "DELETE FROM tag WHERE id=?";
    private final static String FIND_ALL_SQL = "SELECT id, name FROM tag ORDER BY id";
    private final static String IS_TAG_EXIST_SQL = "SELECT id FROM tag WHERE name=?";
    private final static String DELETE_TAG_CERTIFICATE_SQL =
            "DELETE FROM tag_certificate WHERE id_tag=? AND id_certificate=?";
    private final static String TAG_CERTIFICATE_INSERT_SQL =
            "INSERT INTO tag_certificate (id_tag, id_certificate) VALUES (?, ?)";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tag")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Tag> create(Tag tag) throws DaoException {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("name", tag.getName());
            Number id = jdbcInsert.executeAndReturnKey(parameters);
            tag.setId(id.longValue());
            return Optional.of(tag);
        } catch (DataAccessException e) {
            logger.error("Create tag exception", e);
            throw new DaoException("Create tag exception", e);
        }
    }

    @Override
    public Optional<Tag> read(Long id) throws DaoException {
        try {
            Tag tag = jdbcTemplate.queryForObject(READ_TAG_SQL, new Object[]{id}, new TagMapper());
            return Optional.ofNullable(tag);
        } catch (DataAccessException e) {
            logger.error("Read tag exception", e);
            throw new DaoException("Read tag exception", e);
        }
    }

    @Override
    public Optional<Long> delete(Long id) throws DaoException {
        try {
            int rows = jdbcTemplate.update(DELETE_TAG_SQL, id);
            return rows > 0L ? Optional.of(id) : Optional.empty();
        } catch (DataAccessException e) {
            logger.error("Delete tag exception", e);
            throw new DaoException("Delete tag exception", e);
        }
    }

    @Override
    public Optional<List<Tag>> findAll() throws DaoException {
        try {
            List<Tag> tags = jdbcTemplate.query(FIND_ALL_SQL, new TagMapper());
            return Optional.of(tags);
        } catch (DataAccessException e) {
            logger.error("Find all tag exception", e);
            throw new DaoException("Find all tag exception", e);
        }
    }

    @Override
    public void updateNewTagByCertificate(List<Tag> tags, List<Tag> dbTags, Long id) {
        List<Tag> newTags = tags.stream().filter(t -> !dbTags.contains(t)).collect(Collectors.toList());
        newTags.forEach(newTag -> {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(IS_TAG_EXIST_SQL, newTag.getName());
            if (rowSet.next()) {
                long dbTag = rowSet.getLong("id");
                newTag.setId(dbTag);
            } else {
                Optional<Tag> tagDB = Optional.empty();
                try {
                    create(newTag);
                } catch (DaoException e) {
                    logger.error("Update tag exception with name=" + newTag.getName());
                }
                tagDB.ifPresent(t -> newTag.setId(t.getId()));
            }
            jdbcTemplate.update(TAG_CERTIFICATE_INSERT_SQL, newTag.getId(), id);
        });
    }

    @Override
    public void updateOldTagByCertificate(List<Tag> tags, List<Tag> dbTags, Long id) {
        List<Tag> oldTags = dbTags.stream().filter(t -> !tags.contains(t)).collect(Collectors.toList());
        oldTags.forEach(oldTag -> jdbcTemplate.update(DELETE_TAG_CERTIFICATE_SQL, oldTag.getId(), id));
    }
}
