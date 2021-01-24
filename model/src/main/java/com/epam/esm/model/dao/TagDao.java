package com.epam.esm.model.dao;

import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    Optional<Tag> create(Tag tag) throws DaoException;

    Optional<Tag> read(Long id) throws DaoException;

    Optional<Long> delete(Long id) throws DaoException;

    Optional<List<Tag>> findAll() throws DaoException;

    void updateNewTagByCertificate(List<Tag> tags, List<Tag> dbTags, Long id);

    void updateOldTagByCertificate(List<Tag> tags, List<Tag> dbTags, Long id);
}
