package com.epam.esm.controller;

import com.epam.esm.model.dao.exception.NotExistEntityException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Tag RestAPI.
 */
@RestController
public class TagController {

    private TagService tagService;

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
    public ResponseEntity<TagDTO> find(@PathVariable(name = "id") Long id) throws ServiceException {
        TagDTO tag = tagService.find(id);
        return ResponseEntity.ok(tag);
    }

    /**
     * Add tag.
     *
     * @param tagName the tag name
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @PostMapping(value = "/tag", consumes = "application/json")
    public ResponseEntity<TagDTO> add(@RequestBody String tagName) throws ServiceException {
        TagDTO tag = new TagDTO();
        tag.setName(tagName);
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
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) throws ServiceException, NotExistEntityException {
        tagService.delete(id);
        return ResponseEntity.ok("Delete successful!");
    }
}
