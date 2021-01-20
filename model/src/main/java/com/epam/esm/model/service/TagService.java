package com.epam.esm.model.service;

import com.epam.esm.model.dao.exception.NotExistEntityException;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.ServiceException;

public interface TagService {

    TagDTO find(Long id) throws ServiceException;

    TagDTO add(TagDTO tagDTO) throws ServiceException;

    void delete(Long id) throws ServiceException, NotExistEntityException;
}
