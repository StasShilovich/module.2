package com.epam.esm.model.service;

import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.dao.exception.NotExistEntityException;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;

import java.util.List;

public interface GiftCertificateService {

    CertificateDTO find(Long id) throws ServiceException;

    CertificateDTO add(CertificateDTO certificateDTO) throws ServiceException;

    CertificateDTO update(CertificateDTO certificateDTO) throws ServiceException;

    void delete(Long id) throws ServiceException, NotExistEntityException;

    List<CertificateDTO> searchByParameter(String tagName, String part)
            throws ServiceException, UnsupportedOperationException;

    List<CertificateDTO> sortByParameter(String parameter, SortType sortType)
            throws ServiceException, UnsupportedOperationException;
}
