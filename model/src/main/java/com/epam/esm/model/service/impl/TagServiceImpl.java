package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.converter.impl.TagDTOMapper;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final static Logger logger = Logger.getLogger(TagServiceImpl.class);
    private final TagDao tagDao;
    private final TagDTOMapper dtoMapper;

    public TagServiceImpl(TagDao tagDao, TagDTOMapper dtoMapper) {
        this.tagDao = tagDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional
    public TagDTO find(Long id) throws ServiceException {
        try {
            Optional<Tag> tag = tagDao.read(id);
            return tag.map(dtoMapper::toDTO).orElseGet(TagDTO::new);
        } catch (DaoException e) {
            logger.error("Tag with id=" + id + " not exist!", e);
            throw new ServiceException("Tag with id=" + id + " not exist!", e);
        }
    }

    @Override
    @Transactional
    public TagDTO add(TagDTO tagDTO) throws ServiceException {
        try {
            Tag fromDTO = dtoMapper.fromDTO(tagDTO);
            Optional<Tag> tag = tagDao.create(fromDTO);
            tag.orElseThrow(() -> new ServiceException("Invalid tag " + tag));
            return dtoMapper.toDTO(tag.get());
        } catch (DaoException e) {
            logger.error("Add tag service exception", e);
            throw new ServiceException("Add tag service exception", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) throws ServiceException, NotExistEntityException {
        try {
            Optional<Long> deletedId = tagDao.delete(id);
            deletedId.orElseThrow(() -> new NotExistEntityException("Not exist entity with id " + id));
        } catch (DaoException e) {
            logger.error("Delete tag service exception", e);
            throw new ServiceException("Delete tag service exception", e);
        }
    }

    @Override
    @Transactional
    public List<TagDTO> findAll() throws ServiceException {
        try {
            Optional<List<Tag>> tags = tagDao.findAll();
            return tags.map(t -> t.stream()
                    .map(dtoMapper::toDTO)
                    .collect(Collectors.toList())).orElseGet(ArrayList::new);
        } catch (DaoException e) {
            logger.error("Find all tag service exception", e);
            throw new ServiceException("Find all tag service exception", e);
        }
    }
}
