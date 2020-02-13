package com.eyeson.customer_holder.service.impl;

import com.eyeson.customer_holder.service.CustomerAppService;
import com.eyeson.customer_holder.domain.CustomerApp;
import com.eyeson.customer_holder.repository.CustomerAppRepository;
import com.eyeson.customer_holder.repository.search.CustomerAppSearchRepository;
import com.eyeson.customer_holder.service.dto.CustomerAppDTO;
import com.eyeson.customer_holder.service.mapper.CustomerAppMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link CustomerApp}.
 */
@Service
public class CustomerAppServiceImpl implements CustomerAppService {

    private final Logger log = LoggerFactory.getLogger(CustomerAppServiceImpl.class);

    private final CustomerAppRepository customerAppRepository;

    private final CustomerAppMapper customerAppMapper;

    private final CustomerAppSearchRepository customerAppSearchRepository;

    public CustomerAppServiceImpl(CustomerAppRepository customerAppRepository, CustomerAppMapper customerAppMapper, CustomerAppSearchRepository customerAppSearchRepository) {
        this.customerAppRepository = customerAppRepository;
        this.customerAppMapper = customerAppMapper;
        this.customerAppSearchRepository = customerAppSearchRepository;
    }

    /**
     * Save a customerApp.
     *
     * @param customerAppDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CustomerAppDTO save(CustomerAppDTO customerAppDTO) {
        log.debug("Request to save CustomerApp : {}", customerAppDTO);
        CustomerApp customerApp = customerAppMapper.toEntity(customerAppDTO);
        customerApp = customerAppRepository.save(customerApp);
        CustomerAppDTO result = customerAppMapper.toDto(customerApp);
        customerAppSearchRepository.save(customerApp);
        return result;
    }

    /**
     * Get all the customerApps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<CustomerAppDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerApps");
        return customerAppRepository.findAll(pageable)
            .map(customerAppMapper::toDto);
    }

    /**
     * Get one customerApp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<CustomerAppDTO> findOne(String id) {
        log.debug("Request to get CustomerApp : {}", id);
        return customerAppRepository.findById(id)
            .map(customerAppMapper::toDto);
    }

    /**
     * Delete the customerApp by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete CustomerApp : {}", id);
        customerAppRepository.deleteById(id);
        customerAppSearchRepository.deleteById(id);
    }

    /**
     * Search for the customerApp corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<CustomerAppDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerApps for query {}", query);
        return customerAppSearchRepository.search(queryStringQuery(query), pageable)
            .map(customerAppMapper::toDto);
    }
}
