package com.epam.esm.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Tag implements Comparable<Tag> {

    @EqualsAndHashCode.Exclude
    private Long id;
    private String name;

    @Override
    public int compareTo(Tag tag) {
        return name.compareTo(tag.getName());
    }
}
