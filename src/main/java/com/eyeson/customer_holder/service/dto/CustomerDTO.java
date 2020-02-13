package com.eyeson.customer_holder.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import com.eyeson.customer_holder.domain.enumeration.CustomerType;
import com.eyeson.customer_holder.domain.enumeration.CustomerStatus;

/**
 * A DTO for the {@link com.eyeson.customer_holder.domain.Customer} entity.
 */
public class CustomerDTO implements Serializable {

    private String id;

    private String title;

    private ZonedDateTime createDate;

    private Long userId;

    private CustomerType customerType;

    private CustomerStatus status;

    private Boolean isDeleted;

    private String description;

    private String paymentConfig;

    private String notificationConfig;


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

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentConfig() {
        return paymentConfig;
    }

    public void setPaymentConfig(String paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    public String getNotificationConfig() {
        return notificationConfig;
    }

    public void setNotificationConfig(String notificationConfig) {
        this.notificationConfig = notificationConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (customerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", userId=" + getUserId() +
            ", customerType='" + getCustomerType() + "'" +
            ", status='" + getStatus() + "'" +
            ", isDeleted='" + isIsDeleted() + "'" +
            ", description='" + getDescription() + "'" +
            ", paymentConfig='" + getPaymentConfig() + "'" +
            ", notificationConfig='" + getNotificationConfig() + "'" +
            "}";
    }
}
