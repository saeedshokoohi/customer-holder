package com.eyeson.customer_holder.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.eyeson.customer_holder.domain.CustomerApp} entity.
 */
public class CustomerAppDTO implements Serializable {

    private String id;

    private String title;

    private String description;


    private String customerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerAppDTO customerAppDTO = (CustomerAppDTO) o;
        if (customerAppDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerAppDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerAppDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", customerId=" + getCustomerId() +
            "}";
    }
}
