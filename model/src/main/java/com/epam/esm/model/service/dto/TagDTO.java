package com.epam.esm.model.service.dto;

public class TagDTO {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TagDTO() {
    }

    public TagDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("TagDTO{");
        builder.append("id=").append(id);
        builder.append(", name='").append(name).append('\'');
        builder.append('}');
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagDTO)) {
            return false;
        }
        TagDTO tagDTO = (TagDTO) o;
        if (getId() != null ? !getId().equals(tagDTO.getId()) : tagDTO.getId() != null) {
            return false;
        }
        return getName() != null ? getName().equals(tagDTO.getName()) : tagDTO.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
