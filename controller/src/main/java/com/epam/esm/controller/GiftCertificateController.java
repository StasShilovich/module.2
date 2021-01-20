package com.epam.esm.controller;

import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.dao.exception.NotExistEntityException;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<CertificateDTO> find(@PathVariable(name = "id") Long id) throws ServiceException {
        CertificateDTO certificateDTO = certificateService.find(id);
        return ResponseEntity.ok(certificateDTO);
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
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) throws ServiceException, NotExistEntityException {
        certificateService.delete(id);
        return ResponseEntity.ok("Delete successful!");
    }

    /**
     * Find certificate by tag name.
     *
     * @param tagName the tag name
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/certificates/search/{tag-name}")
    public ResponseEntity<List<CertificateDTO>> findByTag(@PathVariable(name = "tag-name") String tagName)
            throws ServiceException {
        List<CertificateDTO> certificates = certificateService.findByTag(tagName);
        return ResponseEntity.ok(certificates);
    }

    /**
     * Search certificate by name or description part.
     *
     * @param part the part
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/certificates/search/{part}")
    public ResponseEntity<List<CertificateDTO>> searchByNameOrDesc(@PathVariable(name = "part") String part)
            throws ServiceException {
        List<CertificateDTO> certificates = certificateService.searchByNameOrDesc(part);
        return ResponseEntity.ok(certificates);
    }

    /**
     * Sort certificates by name.
     *
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/certificates/sort/name/{sort}")
    public ResponseEntity<List<CertificateDTO>> sortByName(@PathVariable(name = "sort") SortType sortType) throws ServiceException {
        List<CertificateDTO> certificates = certificateService.sortByName(sortType);
        return ResponseEntity.ok(certificates);
    }

    /**
     * Sort certificates by date.
     *
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/certificates/sort/date/{sort}")
    public ResponseEntity<List<CertificateDTO>> sortByDate(@PathVariable(name = "sort") SortType sortType) throws ServiceException {
        List<CertificateDTO> certificates = certificateService.sortByDate(sortType);
        return ResponseEntity.ok(certificates);
    }
}
