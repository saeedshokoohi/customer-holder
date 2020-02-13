package com.eyeson.customer_holder.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    public void setUp() {
        customerMapper = new CustomerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(customerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(customerMapper.fromId(null)).isNull();
    }
}
