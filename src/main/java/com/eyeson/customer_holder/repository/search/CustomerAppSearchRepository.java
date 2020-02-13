package com.eyeson.customer_holder.repository.search;

import com.eyeson.customer_holder.domain.CustomerApp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CustomerApp} entity.
 */
public interface CustomerAppSearchRepository extends ElasticsearchRepository<CustomerApp, String> {
}
