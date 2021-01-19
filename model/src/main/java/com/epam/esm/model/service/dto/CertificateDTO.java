package com.epam.esm.model.service.dto;

import java.math.BigDecimal;

public class CertificateDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private String createDate;
    private String lastUpdateDate;

    public CertificateDTO() {
    }

    public CertificateDTO(Long id, String name, String description, BigDecimal price,
                          Integer duration, String createDate, String lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("CertificateDTO{");
        builder.append("id=").append(id);
        builder.append(", name='").append(name).append('\'');
        builder.append(", description='").append(description).append('\'');
        builder.append(", price=").append(price);
        builder.append(", duration=").append(duration);
        builder.append(", createDate='").append(createDate).append('\'');
        builder.append(", lastUpdateDate='").append(lastUpdateDate).append('\'');
        builder.append('}');
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CertificateDTO)) {
            return false;
        }
        CertificateDTO that = (CertificateDTO) o;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) :
                that.getDescription() != null) {
            return false;
        }
        if (getPrice() != null ? !getPrice().equals(that.getPrice()) : that.getPrice() != null) {
            return false;
        }
        if (getDuration() != null ? !getDuration().equals(that.getDuration()) : that.getDuration() != null) {
            return false;
        }
        if (getCreateDate() != null ? !getCreateDate().equals(that.getCreateDate()) : that.getCreateDate() != null) {
            return false;
        }
        return getLastUpdateDate() != null ? getLastUpdateDate().equals(that.getLastUpdateDate()) :
                that.getLastUpdateDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        result = 31 * result + (getDuration() != null ? getDuration().hashCode() : 0);
        result = 31 * result + (getCreateDate() != null ? getCreateDate().hashCode() : 0);
        result = 31 * result + (getLastUpdateDate() != null ? getLastUpdateDate().hashCode() : 0);
        return result;
    }
}
