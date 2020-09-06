package com.spring.brewery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.brewery.model.CustomerDto;
import com.spring.brewery.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    CustomerDto customer;

    MediaType contentType;

    @Before
    public void setUp() {
        contentType = new MediaType("application", "hal+json", StandardCharsets.UTF_8);
        customer = CustomerDto.builder().id(UUID.randomUUID()).name("Los Infernales de Güemes").build();
    }

    @Test
    public void getCustomer() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(customer);

        mockMvc.perform(get("/api/v1/customer/" + customer.getId().toString()).accept(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(customer.getId().toString())))
                .andExpect(jsonPath("$.name", is("Los Infernales de Güemes")));
    }

    @Test
    public void handlePost() throws Exception {
        CustomerDto customerDto = customer;
        customerDto.setId(null);
        CustomerDto savedDto = CustomerDto.builder().id(UUID.randomUUID()).name("Tío Sam").build();
        String customerJSON = objectMapper.writeValueAsString(customerDto);

        given(customerService.saveNewCustomer(any())).willReturn(savedDto);

        mockMvc.perform(post("/api/v1/customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void handleUpdate() throws Exception{
        CustomerDto customerDto = customer;
        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(put("/api/v1/customer/" + customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoJson))
                .andExpect(status().isNoContent());

        then(customerService).should().updateCustomer(any(), any());
    }

    @Test
    public void deleteCustomer() throws Exception {
        CustomerDto customerDto = customer;
        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(delete("/api/v1/customer/" + customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoJson))
                .andExpect(status().isNoContent());

        then(customerService).should().deleteCustomerById(any());
    }
}