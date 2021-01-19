package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.converter.impl.CertificateConverter;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final static Logger logger = Logger.getLogger(GiftCertificateServiceImpl.class);
    private final GiftCertificateDao certificateDao;
    private final CertificateConverter certificateConverter;

    public GiftCertificateServiceImpl(GiftCertificateDao certificateDao, CertificateConverter certificateConverter) {
        this.certificateDao = certificateDao;
        this.certificateConverter = certificateConverter;
    }

    @Override
    public CertificateDTO find(Long id) throws ServiceException {
        try {
            GiftCertificate certificate = certificateDao.read(id);
            CertificateDTO certificateDTO = certificateConverter.toDTO(certificate);
            return certificateDTO;
        } catch (DaoException e) {
            logger.error("Find certificate service exception", e);
            throw new ServiceException("Find certificate service exception", e);
        }
    }

    @Override
    @Transactional
    public CertificateDTO add(CertificateDTO certificateDTO) throws ServiceException {
        try {
            GiftCertificate certificate = certificateConverter.fromDTO(certificateDTO);
            GiftCertificate addCertificate = certificateDao.create(certificate);
            return certificateConverter.toDTO(addCertificate);
        } catch (DaoException e) {
            logger.error("Add certificate service exception", e);
            throw new ServiceException("Add certificate service exception", e);
        }
    }

    @Override
    @Transactional
    public CertificateDTO update(CertificateDTO certificateDTO) throws ServiceException {
        try {
            long id = certificateDao.update(certificateDTO);
            if (id == -1L) {
                return new CertificateDTO();
            }
            GiftCertificate certificate = certificateDao.read(id);
            return certificateConverter.toDTO(certificate);
        } catch (DaoException e) {
            logger.error("Update certificate service exception", e);
            throw new ServiceException("Update certificate service exception", e);
        }
    }

    @Override
    @Transactional
    public long delete(Long id) throws ServiceException {
        try {
            long result = certificateDao.delete(id);
            return result;
        } catch (DaoException e) {
            logger.error("Delete certificate service exception", e);
            throw new ServiceException("Delete certificate service exception", e);
        }
    }

    @Override
    public List<CertificateDTO> findByTag(String tagName) throws ServiceException {
        try {
            List<GiftCertificate> byTag = certificateDao.findByTag(tagName);
            return byTag.stream().map(certificateConverter::toDTO).collect(Collectors.toList());
        } catch (DaoException e) {
            logger.error("Find by tag service exception", e);
            throw new ServiceException("Find by tag service exception", e);
        }
    }

    @Override
    public List<CertificateDTO> searchByNameOrDesc(String part) throws ServiceException {
        try {
            List<GiftCertificate> certificates = certificateDao.searchByNameOrDesc(part);
            return certificates.stream().map(certificateConverter::toDTO).collect(Collectors.toList());
        } catch (DaoException e) {
            logger.error("Search by name or description exception", e);
            throw new ServiceException("Search by name or description exception", e);
        }
    }

    @Override
    public List<CertificateDTO> sortByName(SortType sortType) throws ServiceException {
        try {
            List<GiftCertificate> certificates = certificateDao.sortByName(sortType);
            return certificates.stream().map(certificateConverter::toDTO).collect(Collectors.toList());
        } catch (DaoException e) {
            logger.error("Sort by name exception", e);
            throw new ServiceException("Sort by name exception", e);
        }
    }

    @Override
    public List<CertificateDTO> sortByDate(SortType sortType) throws ServiceException {
        try {
            List<GiftCertificate> certificates = certificateDao.sortByDate(sortType);
            return certificates.stream().map(certificateConverter::toDTO).collect(Collectors.toList());
        } catch (DaoException e) {
            logger.error("Sort by date exception", e);
            throw new ServiceException("Sort by date exception", e);
        }
    }
}
