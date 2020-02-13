package com.eyeson.customer_holder.service;

import com.eyeson.customer_holder.service.dto.CustomerAppDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.eyeson.customer_holder.domain.CustomerApp}.
 */
public interface CustomerAppService {

    /**
     * Save a customerApp.
     *
     * @param customerAppDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerAppDTO save(CustomerAppDTO customerAppDTO);

    /**
     * Get all the customerApps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomerAppDTO> findAll(Pageable pageable);

    /**
     * Get the "id" customerApp.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerAppDTO> findOne(String id);

    /**
     * Delete the "id" customerApp.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Search for the customerApp corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomerAppDTO> search(String query, Pageable pageable);
}
