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
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final static Logger logger = Logger.getLogger(GiftCertificateServiceImpl.class);
    private final GiftCertificateDao certificateDao;
    private final GiftCertificateDTOMapper dtoMapper;

    public GiftCertificateServiceImpl(GiftCertificateDao certificateDao, GiftCertificateDTOMapper dtoMapper) {
        this.certificateDao = certificateDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public CertificateDTO find(Long id) throws ServiceException {
        try {
            GiftCertificate certificate = certificateDao.read(id);
            CertificateDTO certificateDTO = dtoMapper.toDTO(certificate);
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
            GiftCertificate certificate = dtoMapper.fromDTO(certificateDTO);
            GiftCertificate addCertificate = certificateDao.create(certificate);
            return dtoMapper.toDTO(addCertificate);
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
            return dtoMapper.toDTO(certificate);
        } catch (DaoException e) {
            logger.error("Update certificate service exception", e);
            throw new ServiceException("Update certificate service exception", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) throws ServiceException, NotExistEntityException {
        try {
            long deletedId = certificateDao.delete(id);
            if (deletedId == -1L) {
                throw new NotExistEntityException("Not exist entity with id " + id);
            }
        } catch (DaoException e) {
            logger.error("Delete certificate service exception", e);
            throw new ServiceException("Delete certificate service exception", e);
        }
    }

    @Override
    public List<CertificateDTO> searchByParameter(String tagName, String part) throws ServiceException, UnsupportedOperationException {
        try {
            if (tagName != null && part != null || tagName == null && part == null) {
                throw new UnsupportedOperationException("Not supported yet!");
            }
            List<GiftCertificate> result;
            if (tagName == null) {
                result = certificateDao.searchByNameOrDesc(part);
            } else {
                result = certificateDao.findByTag(tagName);
            }
            return result.stream().map(dtoMapper::toDTO).collect(Collectors.toList());
        } catch (DaoException e) {
            logger.error("Search by parameter exception", e);
            throw new ServiceException("Search by parameter exception", e);
        }
    }

    @Override
    public List<CertificateDTO> sortByParameter(String parameter, SortType sortType) throws ServiceException, UnsupportedOperationException {
        try {
            List<GiftCertificate> certificates;
            if (sortType == null) {
                sortType = SortType.ASC;
            }
            if (parameter != null && parameter.equals("name")) {
                certificates = certificateDao.sortByName(sortType);
            } else if (parameter != null && parameter.equals("date")) {
                certificates = certificateDao.sortByDate(sortType);
            } else {
                certificates = certificateDao.sortByName(sortType);
            }
            return certificates.stream().map(dtoMapper::toDTO).collect(Collectors.toList());
        } catch (DaoException e) {
            logger.error("Sort by parameter exception", e);
            throw new ServiceException("Sort by parameter exception", e);
        }
    }
}
