package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.dto.CertificateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GiftCertificateDaoImplTest {

    JdbcTemplate jdbcTemplate;
    DataSource dataSource;
    GiftCertificateDao certificateDao;
    GiftCertificate certificate;
    GiftCertificate expectedTwo;
    GiftCertificate expectedOne;

    @BeforeEach
    void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("classpath:certificate-schema.sql")
                .addScript("classpath:certificate-test-data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        certificateDao = new GiftCertificateDaoImpl(jdbcTemplate);
        certificate = new GiftCertificate(null, "name", "description", new BigDecimal("20"),
                5, LocalDateTime.parse("2021-01-14T13:10:00"), LocalDateTime.parse("2022-01-14T13:10:00"));
        expectedTwo = new GiftCertificate(2L, "Christmas Gift",
                "Entitle a discount of 50$ on purchasing gift item", new BigDecimal("15.00"), 10,
                LocalDateTime.parse("2020-12-20T10:15:00"), LocalDateTime.parse("2021-01-05T18:25:00"));
        expectedOne = new GiftCertificate(1L, "Group meditation session",
                "Include 30 minute guided meditation and empower hour", new BigDecimal("10.00"), 2,
                LocalDateTime.parse("2021-01-12T15:15:00"), LocalDateTime.parse("2021-01-14T13:10:00"));
    }

    @Test
    void testCreatePositive() throws DaoException {
        GiftCertificate actual = certificateDao.create(this.certificate);
        assertEquals(actual, certificate);
    }

    @Test
    void testCreateException() {
        assertThrows(DaoException.class, () -> certificateDao.create(new GiftCertificate()));
    }

    @Test
    void testReadPositive() throws DaoException {
        GiftCertificate actual = certificateDao.read(2L);
        assertEquals(actual, expectedTwo);
    }

    @Test
    void testReadNegative() throws DaoException {
        GiftCertificate actual = certificateDao.read(2L);
        assertNotEquals(actual, certificate);
    }

    @Test
    void testReadException() {
        assertThrows(DaoException.class, () -> certificateDao.read(20L));
    }

    @Test
    void testUpdatePositive() throws DaoException {
        CertificateDTO certificateDTO = new CertificateDTO(1L, "Group meditation", null, null,
                null, null, null);
        long actual = certificateDao.update(certificateDTO);
        assertEquals(actual, 1L);
    }

    @Test
    void testDeletePositive() throws DaoException {
        long actual = certificateDao.delete(2L);
        assertEquals(actual, 2L);
    }

    @Test
    void testSortByDatePositive() throws DaoException {
        List<GiftCertificate> actual = certificateDao.sortByDate(SortType.ASC);
        List<GiftCertificate> expected = new ArrayList<>();
        expected.add(expectedTwo);
        expected.add(expectedOne);
        assertEquals(actual, expected);
    }

    @Test
    void testSortByNamePositive() throws DaoException {
        List<GiftCertificate> actual = certificateDao.sortByName(SortType.ASC);
        List<GiftCertificate> expected = new ArrayList<>();
        expected.add(expectedTwo);
        expected.add(expectedOne);
        assertEquals(actual, expected);
    }
}
