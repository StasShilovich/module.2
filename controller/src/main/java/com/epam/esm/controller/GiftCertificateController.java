package com.epam.esm.controller;

import com.epam.esm.model.dao.entity.SortType;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gift certificate RestAPI.
 */
@RestController
@RequestMapping("/certificate")
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
    @GetMapping("/{id}")
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
    @PostMapping(value = "/")
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
    @PutMapping(value = "/", consumes = "application/json")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) throws ServiceException {
        long result = certificateService.delete(id);
        return result != -1L ? ResponseEntity.ok("Delete successful!") : ResponseEntity.ok("Delete unsuccessful!");
    }

    /**
     * Find certificate by tag name.
     *
     * @param tagName the tag name
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/byName/{name}")
    public ResponseEntity<List<CertificateDTO>> findByTag(@PathVariable(name = "name") String tagName)
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
    @GetMapping("/byPart/{part}")
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
    @GetMapping("/sortByName{sort}")
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
    @GetMapping("/sortByDate{sort}")
    public ResponseEntity<List<CertificateDTO>> sortByDate(@PathVariable(name = "sort") SortType sortType) throws ServiceException {
        List<CertificateDTO> certificates = certificateService.sortByDate(sortType);
        return ResponseEntity.ok(certificates);
    }
}
