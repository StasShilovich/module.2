package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.converter.impl.GiftCertificateDTOMapper;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Transactional
    public CertificateDTO find(Long id) throws ServiceException, NotExistEntityException {
        try {
            Optional<GiftCertificate> certificate = certificateDao.read(id);
            certificate.orElseThrow(() ->
                    new NotExistEntityException("Gift certificate with id=" + id + " not exist!"));
            return dtoMapper.toDTO(certificate.get());
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
            Optional<GiftCertificate> giftCertificate = certificateDao.create(certificate);
            giftCertificate.orElseThrow(() -> new ServiceException("Invalid gift certificate " + certificateDTO));
            return dtoMapper.toDTO(giftCertificate.get());
        } catch (DaoException e) {
            logger.error("Add certificate service exception", e);
            throw new ServiceException("Add certificate service exception", e);
        }
    }

    @Override
    @Transactional
    public CertificateDTO update(CertificateDTO certificateDTO) throws ServiceException {
        try {
            GiftCertificate certificate = dtoMapper.fromDTO(certificateDTO);
            certificateDao.update(certificate);
            Optional<GiftCertificate> giftCertificate = certificateDao.read(certificateDTO.getId());
            giftCertificate.orElseThrow(
                    () -> new ServiceException("Update certificate exception with id=" + certificateDTO.getId()));
            return dtoMapper.toDTO(giftCertificate.get());
        } catch (DaoException e) {
            logger.error("Update certificate service exception", e);
            throw new ServiceException("Update certificate service exception", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) throws ServiceException, NotExistEntityException {
        try {
            Optional<Long> deleteId = certificateDao.delete(id);
            deleteId.orElseThrow(() -> new NotExistEntityException("Not exist entity with id=" + id));
        } catch (DaoException e) {
            logger.error("Delete certificate service exception", e);
            throw new ServiceException("Delete certificate service exception", e);
        }
    }

    @Override
    @Transactional
    public List<CertificateDTO> filterByParameters(String tag, String part, String sortBy, SortType type)
            throws ServiceException {
        try {
            Optional<List<GiftCertificate>> certificates = certificateDao.filterByParameters(tag, part, sortBy, type);
            return certificates.map(c -> c.stream().map(dtoMapper::toDTO).collect(Collectors.toList()))
                    .orElseGet(ArrayList::new);
        } catch (DaoException e) {
            logger.error("Filter by parameters exception", e);
            throw new ServiceException("Filter by parameters exception", e);
        }
    }
}
