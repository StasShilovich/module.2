package com.epam.esm.model.dao.mapper;

import com.epam.esm.model.dao.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<Tag> {

    public Tag mapRow(ResultSet set, int i) throws SQLException {
        return Tag.builder()
                .id(set.getLong("id"))
                .name(set.getString("name")).build();
    }
}
