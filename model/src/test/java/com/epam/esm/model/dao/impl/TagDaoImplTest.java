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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class TagDaoImplTest {

    JdbcTemplate jdbcTemplate;
    DataSource dataSource;
    TagDao tagDao;
    Tag one;
    Tag two;
    Tag three;
    Tag four;
    Tag five;
    Tag six;

    @BeforeEach
    void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("classpath:tag-schema.sql")
                .addScript("classpath:tag-test-data.sql")
                .addScript("classpath:tag-certificate-schema.sql")
                .addScript("classpath:tag-certificate-test-data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        tagDao = new TagDaoImpl(jdbcTemplate);
        one = new Tag(1L, "tourism");
        two = new Tag(2L, "sport");
        three = new Tag(3L, "movie");
        four = new Tag(4L, "relaxation");
        five = new Tag(5L, "christmas");
        six = new Tag(6L, "shopping");
    }

    @Test
    void testCreatePositive() throws DaoException {
        Tag tag = Tag.builder().name("new tag").build();
        Optional<Tag> actual = tagDao.create(tag);
        Tag expected = new Tag(7L, "new tag");
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    void testCreateException() {
        assertThrows(DaoException.class, () -> tagDao.create(new Tag()));
    }

    @Test
    void testReadPositive() throws DaoException {
        Optional<Tag> actual = tagDao.read(1L);
        assertEquals(Optional.of(one), actual);
    }

    @Test
    void testReadException() {
        assertThrows(DaoException.class, () -> tagDao.read(11L));
    }

    @Test
    void testDeletePositive() throws DaoException {
        Optional<Long> actual = tagDao.delete(2L);
        assertEquals(Optional.of(2L), actual);
    }

    @Test
    void testDeleteNegative() throws DaoException {
        Optional<Long> actual = tagDao.delete(11L);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void testFindAll() throws DaoException {
        Optional<List<Tag>> actual = tagDao.findAll();
        List<Tag> expected = new ArrayList<>();
        expected.add(one);
        expected.add(two);
        expected.add(three);
        expected.add(four);
        expected.add(five);
        expected.add(six);
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    void testNewTagUpdating() {

    }
}
