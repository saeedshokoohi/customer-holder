package com.eyeson.customer_holder.web.rest;

import com.eyeson.customer_holder.CustomerHolderApp;
import com.eyeson.customer_holder.domain.CustomerApp;
import com.eyeson.customer_holder.repository.CustomerAppRepository;
import com.eyeson.customer_holder.repository.search.CustomerAppSearchRepository;
import com.eyeson.customer_holder.service.CustomerAppService;
import com.eyeson.customer_holder.service.dto.CustomerAppDTO;
import com.eyeson.customer_holder.service.mapper.CustomerAppMapper;
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


import java.util.Collections;
import java.util.List;

import static com.eyeson.customer_holder.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CustomerAppResource} REST controller.
 */
@SpringBootTest(classes = CustomerHolderApp.class)
public class CustomerAppResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CustomerAppRepository customerAppRepository;

    @Autowired
    private CustomerAppMapper customerAppMapper;

    @Autowired
    private CustomerAppService customerAppService;

    /**
     * This repository is mocked in the com.eyeson.customer_holder.repository.search test package.
     *
     * @see com.eyeson.customer_holder.repository.search.CustomerAppSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomerAppSearchRepository mockCustomerAppSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restCustomerAppMockMvc;

    private CustomerApp customerApp;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerAppResource customerAppResource = new CustomerAppResource(customerAppService);
        this.restCustomerAppMockMvc = MockMvcBuilders.standaloneSetup(customerAppResource)
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
    public static CustomerApp createEntity() {
        CustomerApp customerApp = new CustomerApp()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION);
        return customerApp;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerApp createUpdatedEntity() {
        CustomerApp customerApp = new CustomerApp()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);
        return customerApp;
    }

    @BeforeEach
    public void initTest() {
        customerAppRepository.deleteAll();
        customerApp = createEntity();
    }

    @Test
    public void createCustomerApp() throws Exception {
        int databaseSizeBeforeCreate = customerAppRepository.findAll().size();

        // Create the CustomerApp
        CustomerAppDTO customerAppDTO = customerAppMapper.toDto(customerApp);
        restCustomerAppMockMvc.perform(post("/api/customer-apps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerAppDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerApp in the database
        List<CustomerApp> customerAppList = customerAppRepository.findAll();
        assertThat(customerAppList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerApp testCustomerApp = customerAppList.get(customerAppList.size() - 1);
        assertThat(testCustomerApp.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCustomerApp.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the CustomerApp in Elasticsearch
        verify(mockCustomerAppSearchRepository, times(1)).save(testCustomerApp);
    }

    @Test
    public void createCustomerAppWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerAppRepository.findAll().size();

        // Create the CustomerApp with an existing ID
        customerApp.setId("existing_id");
        CustomerAppDTO customerAppDTO = customerAppMapper.toDto(customerApp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerAppMockMvc.perform(post("/api/customer-apps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerAppDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerApp in the database
        List<CustomerApp> customerAppList = customerAppRepository.findAll();
        assertThat(customerAppList).hasSize(databaseSizeBeforeCreate);

        // Validate the CustomerApp in Elasticsearch
        verify(mockCustomerAppSearchRepository, times(0)).save(customerApp);
    }


    @Test
    public void getAllCustomerApps() throws Exception {
        // Initialize the database
        customerAppRepository.save(customerApp);

        // Get all the customerAppList
        restCustomerAppMockMvc.perform(get("/api/customer-apps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerApp.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    public void getCustomerApp() throws Exception {
        // Initialize the database
        customerAppRepository.save(customerApp);

        // Get the customerApp
        restCustomerAppMockMvc.perform(get("/api/customer-apps/{id}", customerApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerApp.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    public void getNonExistingCustomerApp() throws Exception {
        // Get the customerApp
        restCustomerAppMockMvc.perform(get("/api/customer-apps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCustomerApp() throws Exception {
        // Initialize the database
        customerAppRepository.save(customerApp);

        int databaseSizeBeforeUpdate = customerAppRepository.findAll().size();

        // Update the customerApp
        CustomerApp updatedCustomerApp = customerAppRepository.findById(customerApp.getId()).get();
        updatedCustomerApp
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);
        CustomerAppDTO customerAppDTO = customerAppMapper.toDto(updatedCustomerApp);

        restCustomerAppMockMvc.perform(put("/api/customer-apps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerAppDTO)))
            .andExpect(status().isOk());

        // Validate the CustomerApp in the database
        List<CustomerApp> customerAppList = customerAppRepository.findAll();
        assertThat(customerAppList).hasSize(databaseSizeBeforeUpdate);
        CustomerApp testCustomerApp = customerAppList.get(customerAppList.size() - 1);
        assertThat(testCustomerApp.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCustomerApp.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the CustomerApp in Elasticsearch
        verify(mockCustomerAppSearchRepository, times(1)).save(testCustomerApp);
    }

    @Test
    public void updateNonExistingCustomerApp() throws Exception {
        int databaseSizeBeforeUpdate = customerAppRepository.findAll().size();

        // Create the CustomerApp
        CustomerAppDTO customerAppDTO = customerAppMapper.toDto(customerApp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAppMockMvc.perform(put("/api/customer-apps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerAppDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerApp in the database
        List<CustomerApp> customerAppList = customerAppRepository.findAll();
        assertThat(customerAppList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerApp in Elasticsearch
        verify(mockCustomerAppSearchRepository, times(0)).save(customerApp);
    }

    @Test
    public void deleteCustomerApp() throws Exception {
        // Initialize the database
        customerAppRepository.save(customerApp);

        int databaseSizeBeforeDelete = customerAppRepository.findAll().size();

        // Delete the customerApp
        restCustomerAppMockMvc.perform(delete("/api/customer-apps/{id}", customerApp.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerApp> customerAppList = customerAppRepository.findAll();
        assertThat(customerAppList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CustomerApp in Elasticsearch
        verify(mockCustomerAppSearchRepository, times(1)).deleteById(customerApp.getId());
    }

    @Test
    public void searchCustomerApp() throws Exception {
        // Initialize the database
        customerAppRepository.save(customerApp);
        when(mockCustomerAppSearchRepository.search(queryStringQuery("id:" + customerApp.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(customerApp), PageRequest.of(0, 1), 1));
        // Search the customerApp
        restCustomerAppMockMvc.perform(get("/api/_search/customer-apps?query=id:" + customerApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerApp.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
