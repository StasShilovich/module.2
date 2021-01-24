package com.epam.esm.model.service.converter.impl;

import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.service.converter.DTOMapper;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GiftCertificateDTOMapper implements DTOMapper<CertificateDTO, GiftCertificate> {
    private final static String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.s";

    private final TagDTOMapper tagDTOMapper;

    public GiftCertificateDTOMapper(TagDTOMapper tagDTOMapper) {
        this.tagDTOMapper = tagDTOMapper;
    }

    @Override
    public CertificateDTO toDTO(GiftCertificate certificate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        String formatCreateDate = certificate.getCreateDate().format(formatter);
        String formatLastUpdateDate = certificate.getLastUpdateDate().format(formatter);
        List<TagDTO> tagDTOList = certificate.getTags() != null ?
                certificate.getTags().stream().map(tagDTOMapper::toDTO).collect(Collectors.toList()) : null;
        return CertificateDTO.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .price(certificate.getPrice())
                .createDate(formatCreateDate)
                .lastUpdateDate(formatLastUpdateDate)
                .tags(tagDTOList).build();
    }

    @Override
    public GiftCertificate fromDTO(CertificateDTO certificateDTO) {
        List<Tag> tagList = certificateDTO.getTags() != null ?
                certificateDTO.getTags().stream().map(tagDTOMapper::fromDTO).collect(Collectors.toList()) : null;
        return GiftCertificate.builder()
                .id(certificateDTO.getId())
                .name(certificateDTO.getName())
                .description(certificateDTO.getDescription())
                .duration(certificateDTO.getDuration())
                .price(certificateDTO.getPrice())
                .createDate(StringUtils.isNotEmpty(certificateDTO.getCreateDate()) ?
                        LocalDateTime.parse(certificateDTO.getCreateDate()) : null)
                .lastUpdateDate(StringUtils.isNotEmpty(certificateDTO.getLastUpdateDate()) ?
                        LocalDateTime.parse(certificateDTO.getLastUpdateDate()) : null)
                .tags(tagList).build();
    }
}
