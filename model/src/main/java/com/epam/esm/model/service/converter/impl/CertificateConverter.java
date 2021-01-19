package com.epam.esm.model.service.converter.impl;

import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.service.converter.Converter;
import com.epam.esm.model.service.dto.CertificateDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CertificateConverter implements Converter<CertificateDTO, GiftCertificate> {
    private final static String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.s";

    @Override
    public CertificateDTO toDTO(GiftCertificate certificate) {
        CertificateDTO certificateDTO = new CertificateDTO();
        certificateDTO.setId(certificate.getId());
        certificateDTO.setName(certificate.getName());
        certificateDTO.setDescription(certificate.getDescription());
        certificateDTO.setDuration(certificate.getDuration());
        certificateDTO.setPrice(certificate.getPrice());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        String formatCreateDate = certificate.getCreateDate().format(formatter);
        certificateDTO.setCreateDate(formatCreateDate);
        String formatLastUpdateDate = certificate.getLastUpdateDate().format(formatter);
        certificateDTO.setLastUpdateDate(formatLastUpdateDate);
        return certificateDTO;
    }

    @Override
    public GiftCertificate fromDTO(CertificateDTO certificateDTO) {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(certificateDTO.getId());
        certificate.setName(certificateDTO.getName());
        certificate.setDescription(certificateDTO.getDescription());
        certificate.setDuration(certificateDTO.getDuration());
        certificate.setPrice(certificateDTO.getPrice());
        certificate.setCreateDate(LocalDateTime.parse(certificateDTO.getCreateDate()));
        certificate.setLastUpdateDate(LocalDateTime.parse(certificateDTO.getLastUpdateDate()));
        return certificate;
    }
}
