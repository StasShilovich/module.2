package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.dao.exception.NotExistEntityException;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.converter.impl.GiftCertificateDTOMapper;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class GiftCertificateServiceImplTest {

    @Mock
    GiftCertificateDao dao;
    GiftCertificateDTOMapper converter;
    GiftCertificateService service;
    GiftCertificate certificate;
    CertificateDTO certificateDTO;

    @BeforeEach
    public void setUp() {
        converter = new GiftCertificateDTOMapper();
        service = new GiftCertificateServiceImpl(dao, converter);
        certificate = new GiftCertificate(1L, "name", "description", new BigDecimal("20"),
                5, LocalDateTime.parse("2021-01-14T13:10:00"), LocalDateTime.parse("2022-01-14T13:10:00"));
        certificateDTO = new CertificateDTO(1L, "name", "description", new BigDecimal("20"),
                5, "2021-01-14T13:10:00.0", "2022-01-14T13:10:00.0");
    }

    @Test
    void testFindPositive() throws DaoException, ServiceException {
        lenient().when(dao.read(anyLong())).thenReturn(certificate);
        CertificateDTO actual = service.find(1L);
        assertEquals(actual, certificateDTO);
    }

    @Test
    void testFindNegative() throws DaoException, ServiceException {
        certificate.setName("names");
        lenient().when(dao.read(anyLong())).thenReturn(certificate);
        CertificateDTO actual = service.find(1L);
        assertNotEquals(actual, certificateDTO);
    }

    @Test
    void testFindException() throws DaoException {
        lenient().when(dao.read(anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> service.find(1L));
    }

    @Test
    void testAddPositive() throws DaoException, ServiceException {
        lenient().when(dao.create(any(GiftCertificate.class))).thenReturn(certificate);
        CertificateDTO actual = service.add(certificateDTO);
        assertEquals(actual, certificateDTO);
    }

    @Test
    void testAddNegative() throws DaoException, ServiceException {
        lenient().when(dao.create(any(GiftCertificate.class))).thenReturn(certificate);
        CertificateDTO actual = service.add(certificateDTO);
        assertNotEquals(actual, new CertificateDTO());
    }

    @Test
    void testAddException() throws DaoException {
        lenient().when(dao.create(any(GiftCertificate.class))).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> service.add(certificateDTO));
    }

    @Test
    void testUpdatePositive() throws DaoException, ServiceException {
        lenient().when(dao.update(any(CertificateDTO.class))).thenReturn(1L);
        lenient().when((dao.read(anyLong()))).thenReturn(certificate);
        CertificateDTO actual = service.update(certificateDTO);
        assertEquals(actual, certificateDTO);
    }

    @Test
    void testUpdateNegative() throws DaoException, ServiceException {
        lenient().when(dao.update(any(CertificateDTO.class))).thenReturn(1L);
        lenient().when((dao.read(anyLong()))).thenReturn(certificate);
        CertificateDTO actual = service.update(certificateDTO);
        assertNotEquals(actual, new CertificateDTO());
    }

    @Test
    void testUpdateException() throws DaoException {
        lenient().when(dao.update(any(CertificateDTO.class))).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> service.update(certificateDTO));
    }

    @Test
    void testDeleteException() throws DaoException {
        lenient().when(dao.delete(anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> service.delete(1L));
    }
}
