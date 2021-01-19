package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.dao.mapper.TagMapper;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TagDaoImpl implements TagDao {

    private final static Logger logger = Logger.getLogger(TagDaoImpl.class);
    private final static String READ_TAG_SQL = "SELECT id,name FROM tag WHERE id=?";
    private final static String CREATE_TAG_SQL = "INSERT INTO tag (name) VALUES (?)";
    private final static String DELETE_TAG_SQL = "DELETE FROM tag WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tag")
                .usingGeneratedKeyColumns("id");
    }

    public Tag create(Tag tag) throws DaoException {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("name", tag.getName());
            Number id = jdbcInsert.executeAndReturnKey(parameters);
            tag.setId(id.longValue());
            return tag;
        } catch (DataAccessException e) {
            logger.error("Create tag exception", e);
            throw new DaoException("Create tag exception", e);
        }
    }

    public Tag read(Long id) throws DaoException {
        try {
            Tag tag = jdbcTemplate.queryForObject(READ_TAG_SQL, new Object[]{id}, new TagMapper());
            return tag;
        } catch (DataAccessException e) {
            logger.error("Read tag exception", e);
            throw new DaoException("Read tag exception", e);
        }
    }

    public long delete(Long id) throws DaoException {
        try {
            int rows = jdbcTemplate.update(DELETE_TAG_SQL, id);
            return rows > 0L ? id : -1L;
        } catch (DataAccessException e) {
            logger.error("Delete tag exception", e);
            throw new DaoException("Delete tag exception", e);
        }
    }
}
