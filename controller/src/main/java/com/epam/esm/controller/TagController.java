package com.epam.esm.controller;

import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Tag RestAPI.
 */
@RestController
public class TagController {

    private final TagService tagService;

    private TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Find tag by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/tag/{id}")
    public ResponseEntity<TagDTO> find(@PathVariable(name = "id") Long id) throws ServiceException, NotExistEntityException {
        TagDTO tag = tagService.find(id);
        return ResponseEntity.ok(tag);
    }

    /**
     * Add tag.
     *
     * @param tag tag
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @PostMapping(value = "/tag", consumes = "application/json")
    public ResponseEntity<TagDTO> add(@RequestBody TagDTO tag) throws ServiceException {
        TagDTO result = tagService.add(tag);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete tag by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @DeleteMapping("/tag/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) throws ServiceException, NotExistEntityException {
        tagService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagDTO>> findAll() throws ServiceException {
        List<TagDTO> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }
}
