package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.dao.impl.TagDaoImpl;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.converter.impl.TagConverter;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagServiceImpl implements TagService {

    private final static Logger logger = Logger.getLogger(TagServiceImpl.class);
    private final TagDao tagDao;
    private final TagConverter tagConverter;

    public TagServiceImpl(TagDao tagDao, TagConverter tagConverter) {
        this.tagDao = tagDao;
        this.tagConverter = tagConverter;
    }

    public TagDTO find(Long id) throws ServiceException {
        try {
            Tag tag = tagDao.read(id);
            TagDTO tagDTO = tagConverter.toDTO(tag);
            return tagDTO;
        } catch (DaoException e) {
            logger.error("Find tag service exception", e);
            throw new ServiceException("Find tag service exception", e);
        }
    }

    @Override
    @Transactional
    public TagDTO add(TagDTO tagDTO) throws ServiceException {
        try {
            Tag tag = tagConverter.fromDTO(tagDTO);
            Tag tagDao = this.tagDao.create(tag);
            return tagConverter.toDTO(tagDao);
        } catch (DaoException e) {
            logger.error("Add tag service exception", e);
            throw new ServiceException("Add tag service exception", e);
        }
    }

    @Override
    @Transactional
    public long delete(Long id) throws ServiceException {
        try {
            long isDelete = tagDao.delete(id);
            return isDelete;
        } catch (DaoException e) {
            logger.error("Delete tag service exception", e);
            throw new ServiceException("Delete tag service exception", e);
        }
    }
}
