package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.converter.impl.GiftCertificateDTOMapper;
import com.epam.esm.model.service.converter.impl.TagDTOMapper;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.NotExistEntityException;
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
import java.util.Optional;

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
    GiftCertificateDTOMapper certificateDTOMapper;
    TagDTOMapper tagDTOMapper;
    GiftCertificateService service;
    GiftCertificate certificate;
    CertificateDTO certificateDTO;
    List<GiftCertificate> certificates;

    @BeforeEach
    public void setUp() {
        certificateDTOMapper = new GiftCertificateDTOMapper(tagDTOMapper);
        service = new GiftCertificateServiceImpl(dao, certificateDTOMapper);
        certificate = new GiftCertificate(1L, "name", "description", new BigDecimal("20"),
                5, LocalDateTime.parse("2021-01-14T13:10:00"), LocalDateTime.parse("2022-01-14T13:10:00"), null);
        certificateDTO = new CertificateDTO(1L, "name", "description", new BigDecimal("20"),
                5, "2021-01-14T13:10:00.0", "2022-01-14T13:10:00.0", null);
        certificates = new ArrayList<>();
        certificates.add(certificate);
    }

    @Test
    void testFindPositive() throws DaoException, ServiceException, NotExistEntityException {
        lenient().when(dao.read(anyLong())).thenReturn(Optional.of(certificate));
        CertificateDTO actual = service.find(1L);
        assertEquals(certificateDTO, actual);
    }

    @Test
    void testFindNegative() throws DaoException, ServiceException, NotExistEntityException {
        certificate.setName("names");
        lenient().when(dao.read(anyLong())).thenReturn(Optional.of(certificate));
        CertificateDTO actual = service.find(1L);
        assertNotEquals(certificateDTO, actual);
    }

    @Test
    void testFindServiceException() throws DaoException {
        lenient().when(dao.read(anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> service.find(1L));
    }

    @Test
    void testFindNotExistEntityException() throws DaoException {
        lenient().when(dao.read(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotExistEntityException.class, () -> service.find(1L));
    }

    @Test
    void testAddPositive() throws DaoException, ServiceException {
        lenient().when(dao.create(any(GiftCertificate.class))).thenReturn(Optional.of(certificate));
        CertificateDTO actual = service.add(certificateDTO);
        assertEquals(certificateDTO, actual);
    }

    @Test
    void testAddNegative() throws DaoException, ServiceException {
        lenient().when(dao.create(any(GiftCertificate.class))).thenReturn(Optional.of(certificate));
        CertificateDTO actual = service.add(certificateDTO);
        assertNotEquals(new CertificateDTO(), actual);
    }

    @Test
    void testAddServiceException() throws DaoException {
        lenient().when(dao.create(any(GiftCertificate.class))).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> service.add(certificateDTO));
    }

    @Test
    void testUpdatePositive() throws DaoException, ServiceException {
        lenient().when((dao.read(anyLong()))).thenReturn(Optional.of(certificate));
        CertificateDTO actual = service.update(certificateDTO);
        assertEquals(certificateDTO, actual);
    }

    @Test
    void testUpdateNegative() throws DaoException, ServiceException {
        lenient().when(dao.read(anyLong())).thenReturn(Optional.of(certificate));
        CertificateDTO actual = service.update(certificateDTO);
        assertNotEquals(new CertificateDTO(), actual);
    }

    @Test
    void testUpdateFromDaoException() throws DaoException {
        lenient().when(dao.read(anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> service.update(certificateDTO));
    }

    @Test
    void testUpdateFromServiceException() throws DaoException {
        lenient().when(dao.read(anyLong())).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> service.update(certificateDTO));
    }

    @Test
    void testDeleteServiceException() throws DaoException {
        lenient().when(dao.delete(anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> service.delete(1L));
    }

    @Test
    void testDeleteNotExistEntityException() throws DaoException {
        lenient().when(dao.delete(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotExistEntityException.class, () -> service.delete(1L));
    }

    @Test
    void testFilterByParametersPositive() throws DaoException, ServiceException {
        lenient().when(dao.filterByParameters(anyString(), anyString(), anyString(), any()))
                .thenReturn(Optional.of(certificates));
        List<CertificateDTO> actual = service.filterByParameters("", "", "", null);
        List<CertificateDTO> expected = new ArrayList<>();
        expected.add(certificateDTO);
        assertEquals(expected, actual);
    }

    @Test
    void testFilterByParametersNegative() throws DaoException, ServiceException {
        lenient().when(dao.filterByParameters(anyString(), anyString(), anyString(), any()))
                .thenReturn(Optional.of(certificates));
        List<CertificateDTO> actual = service.filterByParameters("", "", "", null);
        assertNotEquals(new ArrayList<>(), actual);
    }

    @Test
    void testFilterByParametersException() throws DaoException {
        lenient().when(dao.filterByParameters(anyString(), anyString(), anyString(), any()))
                .thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> service.filterByParameters("", "", "", null));
    }
}
