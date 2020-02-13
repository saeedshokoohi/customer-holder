package com.eyeson.customer_holder.repository;

import com.eyeson.customer_holder.domain.CustomerApp;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the CustomerApp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerAppRepository extends MongoRepository<CustomerApp, String> {

}
