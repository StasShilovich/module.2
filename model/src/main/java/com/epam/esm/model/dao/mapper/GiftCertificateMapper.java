package com.epam.esm.model.dao.mapper;

import com.epam.esm.model.dao.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(resultSet.getLong("id"));
        certificate.setName(resultSet.getString("name"));
        certificate.setDescription(resultSet.getString("description"));
        certificate.setPrice(resultSet.getBigDecimal("price"));
        certificate.setDuration(resultSet.getInt("duration"));
        certificate.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
        certificate.setLastUpdateDate(resultSet.getTimestamp("last_update_date").toLocalDateTime());
        return certificate;
    }
}
