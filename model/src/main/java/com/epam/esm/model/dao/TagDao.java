package com.epam.esm.model.dao;

import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;

public interface TagDao {

    Tag create(Tag tag) throws DaoException;

    Tag read(Long id) throws DaoException;

    long delete(Long id) throws DaoException;
}
