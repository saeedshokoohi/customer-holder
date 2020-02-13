package com.eyeson.customer_holder.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;

import com.eyeson.customer_holder.domain.enumeration.CustomerType;

import com.eyeson.customer_holder.domain.enumeration.CustomerStatus;

/**
 * A Customer.
 */
@Document(collection = "customer")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("create_date")
    private ZonedDateTime createDate;

    @Field("user_id")
    private Long userId;

    @Field("customer_type")
    private CustomerType customerType;

    @Field("status")
    private CustomerStatus status;

    @Field("is_deleted")
    private Boolean isDeleted;

    @Field("description")
    private String description;

    @Field("payment_config")
    private String paymentConfig;

    @Field("notification_config")
    private String notificationConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Customer title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public Customer createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Long getUserId() {
        return userId;
    }

    public Customer userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public Customer customerType(CustomerType customerType) {
        this.customerType = customerType;
        return this;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public Customer status(CustomerStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public Customer isDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDescription() {
        return description;
    }

    public Customer description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentConfig() {
        return paymentConfig;
    }

    public Customer paymentConfig(String paymentConfig) {
        this.paymentConfig = paymentConfig;
        return this;
    }

    public void setPaymentConfig(String paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    public String getNotificationConfig() {
        return notificationConfig;
    }

    public Customer notificationConfig(String notificationConfig) {
        this.notificationConfig = notificationConfig;
        return this;
    }

    public void setNotificationConfig(String notificationConfig) {
        this.notificationConfig = notificationConfig;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Customer{" +
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
