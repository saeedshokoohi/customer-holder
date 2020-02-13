package com.eyeson.customer_holder.web.rest;

import com.eyeson.customer_holder.service.CustomerAppService;
import com.eyeson.customer_holder.web.rest.errors.BadRequestAlertException;
import com.eyeson.customer_holder.service.dto.CustomerAppDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.eyeson.customer_holder.domain.CustomerApp}.
 */
@RestController
@RequestMapping("/api")
public class CustomerAppResource {

    private final Logger log = LoggerFactory.getLogger(CustomerAppResource.class);

    private static final String ENTITY_NAME = "customerHolderCustomerApp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerAppService customerAppService;

    public CustomerAppResource(CustomerAppService customerAppService) {
        this.customerAppService = customerAppService;
    }

    /**
     * {@code POST  /customer-apps} : Create a new customerApp.
     *
     * @param customerAppDTO the customerAppDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerAppDTO, or with status {@code 400 (Bad Request)} if the customerApp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-apps")
    public ResponseEntity<CustomerAppDTO> createCustomerApp(@RequestBody CustomerAppDTO customerAppDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerApp : {}", customerAppDTO);
        if (customerAppDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerApp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerAppDTO result = customerAppService.save(customerAppDTO);
        return ResponseEntity.created(new URI("/api/customer-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-apps} : Updates an existing customerApp.
     *
     * @param customerAppDTO the customerAppDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAppDTO,
     * or with status {@code 400 (Bad Request)} if the customerAppDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerAppDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-apps")
    public ResponseEntity<CustomerAppDTO> updateCustomerApp(@RequestBody CustomerAppDTO customerAppDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerApp : {}", customerAppDTO);
        if (customerAppDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerAppDTO result = customerAppService.save(customerAppDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerAppDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /customer-apps} : get all the customerApps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerApps in body.
     */
    @GetMapping("/customer-apps")
    public ResponseEntity<List<CustomerAppDTO>> getAllCustomerApps(Pageable pageable) {
        log.debug("REST request to get a page of CustomerApps");
        Page<CustomerAppDTO> page = customerAppService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-apps/:id} : get the "id" customerApp.
     *
     * @param id the id of the customerAppDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerAppDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-apps/{id}")
    public ResponseEntity<CustomerAppDTO> getCustomerApp(@PathVariable String id) {
        log.debug("REST request to get CustomerApp : {}", id);
        Optional<CustomerAppDTO> customerAppDTO = customerAppService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerAppDTO);
    }

    /**
     * {@code DELETE  /customer-apps/:id} : delete the "id" customerApp.
     *
     * @param id the id of the customerAppDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-apps/{id}")
    public ResponseEntity<Void> deleteCustomerApp(@PathVariable String id) {
        log.debug("REST request to delete CustomerApp : {}", id);
        customerAppService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/customer-apps?query=:query} : search for the customerApp corresponding
     * to the query.
     *
     * @param query the query of the customerApp search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/customer-apps")
    public ResponseEntity<List<CustomerAppDTO>> searchCustomerApps(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CustomerApps for query {}", query);
        Page<CustomerAppDTO> page = customerAppService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
