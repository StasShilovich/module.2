package com.epam.esm.controller;

import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Gift certificate RestAPI.
 */
@RestController
public class GiftCertificateController {

    private final GiftCertificateService certificateService;

    private GiftCertificateController(GiftCertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Find certificate by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/certificate/{id}")
    public ResponseEntity<CertificateDTO> find(@PathVariable(name = "id") Long id)
            throws ServiceException, NotExistEntityException {
        CertificateDTO result = certificateService.find(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Add certificate.
     *
     * @param certificateDTO the certificate dto
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @PostMapping(value = "/certificate")
    public ResponseEntity<CertificateDTO> add(@RequestBody CertificateDTO certificateDTO) throws ServiceException {
        CertificateDTO result = certificateService.add(certificateDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Update certificate. Mark the fields that are not specified for updating null.
     *
     * @param certificateDTO the certificate dto
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @PutMapping(value = "/certificate", consumes = "application/json")
    public ResponseEntity<CertificateDTO> update(@RequestBody CertificateDTO certificateDTO) throws ServiceException {
        CertificateDTO result = certificateService.update(certificateDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete certificate by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @DeleteMapping("/certificate/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id)
            throws ServiceException, NotExistEntityException {
        certificateService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Filter gift certificates by parameters
     *
     * @param tag
     * @param part
     * @param sortBy
     * @param type
     * @return
     * @throws ServiceException
     */
    @GetMapping("/certificates")
    public ResponseEntity<List<CertificateDTO>> filterByParameter(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "part", required = false) String part,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "type", required = false) SortType type)
            throws ServiceException {
        List<CertificateDTO> list = certificateService.filterByParameters(tag, part, sortBy, type);
        return ResponseEntity.ok(list);
    }
}
