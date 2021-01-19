package com.epam.esm.model.service;

import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;

import java.util.List;

public interface GiftCertificateService {

    CertificateDTO find(Long id) throws ServiceException;

    CertificateDTO add(CertificateDTO certificateDTO) throws ServiceException;

    CertificateDTO update(CertificateDTO certificateDTO) throws ServiceException;

    long delete(Long id) throws ServiceException;

    List<CertificateDTO> findByTag(String tagName) throws ServiceException;

    List<CertificateDTO> searchByNameOrDesc(String part) throws ServiceException;

    List<CertificateDTO> sortByName(SortType sortType) throws ServiceException;

    List<CertificateDTO> sortByDate(SortType sortType) throws ServiceException;
}
