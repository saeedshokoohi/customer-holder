package com.eyeson.customer_holder.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CustomerAppSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CustomerAppSearchRepositoryMockConfiguration {

    @MockBean
    private CustomerAppSearchRepository mockCustomerAppSearchRepository;

}
