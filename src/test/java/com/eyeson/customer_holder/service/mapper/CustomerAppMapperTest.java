package com.eyeson.customer_holder.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerAppMapperTest {

    private CustomerAppMapper customerAppMapper;

    @BeforeEach
    public void setUp() {
        customerAppMapper = new CustomerAppMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(customerAppMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(customerAppMapper.fromId(null)).isNull();
    }
}
