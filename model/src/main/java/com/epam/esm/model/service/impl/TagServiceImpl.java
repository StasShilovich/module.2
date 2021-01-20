package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.dao.exception.NotExistEntityException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.converter.impl.TagDTOMapper;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagServiceImpl implements TagService {

    private final static Logger logger = Logger.getLogger(TagServiceImpl.class);
    private final TagDao tagDao;
    private final TagDTOMapper dtoMapper;

    public TagServiceImpl(TagDao tagDao, TagDTOMapper dtoMapper) {
        this.tagDao = tagDao;
        this.dtoMapper = dtoMapper;
    }

    public TagDTO find(Long id) throws ServiceException {
        try {
            Tag tag = tagDao.read(id);
            TagDTO tagDTO = dtoMapper.toDTO(tag);
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
            Tag tag = dtoMapper.fromDTO(tagDTO);
            Tag tagDao = this.tagDao.create(tag);
            return dtoMapper.toDTO(tagDao);
        } catch (DaoException e) {
            logger.error("Add tag service exception", e);
            throw new ServiceException("Add tag service exception", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) throws ServiceException, NotExistEntityException {
        try {
            long deletedId = tagDao.delete(id);
            if (deletedId == -1L) {
                throw new NotExistEntityException("Not exist entity with id " + id);
            }
        } catch (DaoException e) {
            logger.error("Delete tag service exception", e);
            throw new ServiceException("Delete tag service exception", e);
        }
    }
}
