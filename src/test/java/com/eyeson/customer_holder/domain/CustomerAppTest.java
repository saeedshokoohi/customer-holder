package com.eyeson.customer_holder.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.eyeson.customer_holder.web.rest.TestUtil;

public class CustomerAppTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerApp.class);
        CustomerApp customerApp1 = new CustomerApp();
        customerApp1.setId("id1");
        CustomerApp customerApp2 = new CustomerApp();
        customerApp2.setId(customerApp1.getId());
        assertThat(customerApp1).isEqualTo(customerApp2);
        customerApp2.setId("id2");
        assertThat(customerApp1).isNotEqualTo(customerApp2);
        customerApp1.setId(null);
        assertThat(customerApp1).isNotEqualTo(customerApp2);
    }
}
