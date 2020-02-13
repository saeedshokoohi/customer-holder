package com.eyeson.customer_holder.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.eyeson.customer_holder.web.rest.TestUtil;

public class CustomerAppDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAppDTO.class);
        CustomerAppDTO customerAppDTO1 = new CustomerAppDTO();
        customerAppDTO1.setId("id1");
        CustomerAppDTO customerAppDTO2 = new CustomerAppDTO();
        assertThat(customerAppDTO1).isNotEqualTo(customerAppDTO2);
        customerAppDTO2.setId(customerAppDTO1.getId());
        assertThat(customerAppDTO1).isEqualTo(customerAppDTO2);
        customerAppDTO2.setId("id2");
        assertThat(customerAppDTO1).isNotEqualTo(customerAppDTO2);
        customerAppDTO1.setId(null);
        assertThat(customerAppDTO1).isNotEqualTo(customerAppDTO2);
    }
}
