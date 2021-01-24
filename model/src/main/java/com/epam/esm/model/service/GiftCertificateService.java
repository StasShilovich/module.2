package com.epam.esm.model.service;

import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;

import java.util.List;

public interface GiftCertificateService {

    CertificateDTO find(Long id) throws ServiceException, NotExistEntityException;

    CertificateDTO add(CertificateDTO certificateDTO) throws ServiceException;

    CertificateDTO update(CertificateDTO certificateDTO) throws ServiceException;

    void delete(Long id) throws ServiceException, NotExistEntityException;

    List<CertificateDTO> filterByParameters(String tag, String part, String sortBy, SortType type)
            throws ServiceException;
}
