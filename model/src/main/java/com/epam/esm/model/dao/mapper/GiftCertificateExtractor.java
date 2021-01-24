package com.epam.esm.model.dao.mapper;

import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.Tag;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GiftCertificateExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    private final static Logger logger = Logger.getLogger(GiftCertificateExtractor.class);

    @Override
    public List<GiftCertificate> extractData(ResultSet set) throws DataAccessException {
        try {
            List<GiftCertificate> certificates = new ArrayList<>();
            while (set.next()) {
                Tag tag = Tag.builder()
                        .id(set.getLong("t.id"))
                        .name(set.getString("t.name")).build();
                long id = set.getLong("c.id");
                Optional<GiftCertificate> certificateOptional = certificates.stream().filter(c -> c.getId() == id).findFirst();
                if (certificateOptional.isPresent()) {
                    List<Tag> tags = certificateOptional.get().getTags();
                    tags.add(tag);
                } else {
                    List<Tag> tags = new ArrayList<>();
                    tags.add(tag);
                    GiftCertificate certificate = GiftCertificate.builder()
                            .id(set.getLong("c.id"))
                            .name(set.getString("c.name"))
                            .description(set.getString("description"))
                            .price(set.getBigDecimal("price"))
                            .duration(set.getInt("duration"))
                            .createDate(set.getTimestamp("create_date").toLocalDateTime())
                            .lastUpdateDate(set.getTimestamp("last_update_date").toLocalDateTime())
                            .tags(tags)
                            .build();
                    certificates.add(certificate);
                }
            }
            return certificates;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }
}
