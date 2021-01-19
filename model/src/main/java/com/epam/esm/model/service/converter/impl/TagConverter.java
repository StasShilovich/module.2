package com.epam.esm.model.service.converter.impl;

import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.service.converter.Converter;
import com.epam.esm.model.service.dto.TagDTO;
import org.springframework.stereotype.Component;

@Component
public class TagConverter implements Converter<TagDTO, Tag> {

    @Override
    public TagDTO toDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        return tagDTO;
    }

    @Override
    public Tag fromDTO(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        return tag;
    }
}
