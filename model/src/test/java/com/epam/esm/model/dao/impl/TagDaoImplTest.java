package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class TagDaoImplTest {

    JdbcTemplate jdbcTemplate;
    DataSource dataSource;
    TagDao tagDao;

    @BeforeEach
    void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("classpath:tag-schema.sql")
                .addScript("classpath:tag-test-data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        tagDao = new TagDaoImpl(jdbcTemplate);
    }

    @Test
    void testCreatePositive() throws DaoException {
        Tag tag = new Tag();
        tag.setName("new tag");
        Tag actual = this.tagDao.create(tag);
        assertEquals(actual, new Tag(7L,"new tag"));
    }

    @Test
    void read() throws DaoException {
        Tag actual = tagDao.read(1L);
        Tag expected = new Tag(1L, "tourism");
        assertEquals(actual, expected);
    }

    @Test
    void delete() throws DaoException {
        long actual = tagDao.delete(3L);
        assertEquals(actual, 3L);
    }
}
