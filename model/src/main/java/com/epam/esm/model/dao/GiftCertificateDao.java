package com.epam.esm.model.dao;

import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.dao.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {

    Optional<GiftCertificate> create(GiftCertificate certificate) throws DaoException;

    Optional<GiftCertificate> read(Long id) throws DaoException;

    void update(GiftCertificate certificate) throws DaoException;

    Optional<Long> delete(Long id) throws DaoException;

    Optional<List<GiftCertificate>> filterByParameters(String tag, String part, String sortBy, SortType type)
            throws DaoException;
}
