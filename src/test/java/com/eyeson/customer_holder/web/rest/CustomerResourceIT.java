package com.eyeson.customer_holder.web.rest;

import com.eyeson.customer_holder.CustomerHolderApp;
import com.eyeson.customer_holder.domain.Customer;
import com.eyeson.customer_holder.repository.CustomerRepository;
import com.eyeson.customer_holder.repository.search.CustomerSearchRepository;
import com.eyeson.customer_holder.service.CustomerService;
import com.eyeson.customer_holder.service.dto.CustomerDTO;
import com.eyeson.customer_holder.service.mapper.CustomerMapper;
import com.eyeson.customer_holder.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.eyeson.customer_holder.web.rest.TestUtil.sameInstant;
import static com.eyeson.customer_holder.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eyeson.customer_holder.domain.enumeration.CustomerType;
import com.eyeson.customer_holder.domain.enumeration.CustomerStatus;
/**
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@SpringBootTest(classes = CustomerHolderApp.class)
public class CustomerResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final CustomerType DEFAULT_CUSTOMER_TYPE = CustomerType.TRIAL;
    private static final CustomerType UPDATED_CUSTOMER_TYPE = CustomerType.FREE;

    private static final CustomerStatus DEFAULT_STATUS = CustomerStatus.ACTIVE;
    private static final CustomerStatus UPDATED_STATUS = CustomerStatus.EXPIRED;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_CONFIG = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_CONFIG = "BBBBBBBBBB";

    private static final String DEFAULT_NOTIFICATION_CONFIG = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_CONFIG = "BBBBBBBBBB";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;

    /**
     * This repository is mocked in the com.eyeson.customer_holder.repository.search test package.
     *
     * @see com.eyeson.customer_holder.repository.search.CustomerSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomerSearchRepository mockCustomerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerResource customerResource = new CustomerResource(customerService);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity() {
        Customer customer = new Customer()
            .title(DEFAULT_TITLE)
            .createDate(DEFAULT_CREATE_DATE)
            .userId(DEFAULT_USER_ID)
            .customerType(DEFAULT_CUSTOMER_TYPE)
            .status(DEFAULT_STATUS)
            .isDeleted(DEFAULT_IS_DELETED)
            .description(DEFAULT_DESCRIPTION)
            .paymentConfig(DEFAULT_PAYMENT_CONFIG)
            .notificationConfig(DEFAULT_NOTIFICATION_CONFIG);
        return customer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity() {
        Customer customer = new Customer()
            .title(UPDATED_TITLE)
            .createDate(UPDATED_CREATE_DATE)
            .userId(UPDATED_USER_ID)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .status(UPDATED_STATUS)
            .isDeleted(UPDATED_IS_DELETED)
            .description(UPDATED_DESCRIPTION)
            .paymentConfig(UPDATED_PAYMENT_CONFIG)
            .notificationConfig(UPDATED_NOTIFICATION_CONFIG);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customerRepository.deleteAll();
        customer = createEntity();
    }

    @Test
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCustomer.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCustomer.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testCustomer.getCustomerType()).isEqualTo(DEFAULT_CUSTOMER_TYPE);
        assertThat(testCustomer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCustomer.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testCustomer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomer.getPaymentConfig()).isEqualTo(DEFAULT_PAYMENT_CONFIG);
        assertThat(testCustomer.getNotificationConfig()).isEqualTo(DEFAULT_NOTIFICATION_CONFIG);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(1)).save(testCustomer);
    }

    @Test
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId("existing_id");
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(0)).save(customer);
    }


    @Test
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.save(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].paymentConfig").value(hasItem(DEFAULT_PAYMENT_CONFIG)))
            .andExpect(jsonPath("$.[*].notificationConfig").value(hasItem(DEFAULT_NOTIFICATION_CONFIG)));
    }
    
    @Test
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.save(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.customerType").value(DEFAULT_CUSTOMER_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.paymentConfig").value(DEFAULT_PAYMENT_CONFIG))
            .andExpect(jsonPath("$.notificationConfig").value(DEFAULT_NOTIFICATION_CONFIG));
    }

    @Test
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.save(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        updatedCustomer
            .title(UPDATED_TITLE)
            .createDate(UPDATED_CREATE_DATE)
            .userId(UPDATED_USER_ID)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .status(UPDATED_STATUS)
            .isDeleted(UPDATED_IS_DELETED)
            .description(UPDATED_DESCRIPTION)
            .paymentConfig(UPDATED_PAYMENT_CONFIG)
            .notificationConfig(UPDATED_NOTIFICATION_CONFIG);
        CustomerDTO customerDTO = customerMapper.toDto(updatedCustomer);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCustomer.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCustomer.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testCustomer.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCustomer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCustomer.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testCustomer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomer.getPaymentConfig()).isEqualTo(UPDATED_PAYMENT_CONFIG);
        assertThat(testCustomer.getNotificationConfig()).isEqualTo(UPDATED_NOTIFICATION_CONFIG);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(1)).save(testCustomer);
    }

    @Test
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(0)).save(customer);
    }

    @Test
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.save(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(1)).deleteById(customer.getId());
    }

    @Test
    public void searchCustomer() throws Exception {
        // Initialize the database
        customerRepository.save(customer);
        when(mockCustomerSearchRepository.search(queryStringQuery("id:" + customer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(customer), PageRequest.of(0, 1), 1));
        // Search the customer
        restCustomerMockMvc.perform(get("/api/_search/customers?query=id:" + customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].paymentConfig").value(hasItem(DEFAULT_PAYMENT_CONFIG)))
            .andExpect(jsonPath("$.[*].notificationConfig").value(hasItem(DEFAULT_NOTIFICATION_CONFIG)));
    }
}
