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

import javax.ws.rs.QueryParam;
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
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id)
            throws ServiceException, NotExistEntityException {
        certificateService.delete(id);
        return ResponseEntity.ok("Delete successful!");
    }

    /**
     * Search by tag name or description part
     *
     * @param tagName
     * @param part
     * @return
     * @throws ServiceException
     */
    @GetMapping("/certificates/search")
    public ResponseEntity<List<CertificateDTO>> searchByParameter
    (@QueryParam("tagName") String tagName, @QueryParam("part") String part)
            throws ServiceException {
        List<CertificateDTO> certificates = certificateService.searchByParameter(tagName, part);
        return ResponseEntity.ok(certificates);
    }

    /**
     * Sort by date or name ASC/DESC
     *
     * @param by
     * @param type
     * @return
     * @throws ServiceException
     */
    @GetMapping("/certificates/sort")
    public ResponseEntity<List<CertificateDTO>> sortByParameter
    (@QueryParam("by") String by, @QueryParam("type") SortType type)
            throws ServiceException {
        List<CertificateDTO> certificates = certificateService.sortByParameter(by, type);
        return ResponseEntity.ok(certificates);
    }
}
