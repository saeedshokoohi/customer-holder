package com.eyeson.customer_holder.service.mapper;


import com.eyeson.customer_holder.domain.*;
import com.eyeson.customer_holder.service.dto.CustomerAppDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerApp} and its DTO {@link CustomerAppDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface CustomerAppMapper extends EntityMapper<CustomerAppDTO, CustomerApp> {

    @Mapping(source = "customer.id", target = "customerId")
    CustomerAppDTO toDto(CustomerApp customerApp);

    @Mapping(source = "customerId", target = "customer")
    CustomerApp toEntity(CustomerAppDTO customerAppDTO);

    default CustomerApp fromId(String id) {
        if (id == null) {
            return null;
        }
        CustomerApp customerApp = new CustomerApp();
        customerApp.setId(id);
        return customerApp;
    }
}
