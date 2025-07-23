package net.groundgurus.customer_api_jdbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.groundgurus.customer_api_jdbc.model.Customer;
import net.groundgurus.customer_api_jdbc.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@Mock
	private CustomerService customerService;

	@InjectMocks
	private CustomerController customerController;

	private Customer customer;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		objectMapper = new ObjectMapper();

		customer = Customer.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.build();
	}

	@Test
	void getCustomerById_shouldReturnCustomer_whenCustomerExists() throws Exception {
		when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));

		mockMvc.perform(get("/api/customers/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.firstName").value("John"))
				.andExpect(jsonPath("$.lastName").value("Doe"));

		verify(customerService).getCustomerById(1L);
	}

	@Test
	void getCustomerById_shouldReturnNotFound_whenCustomerDoesNotExist() throws Exception {
		when(customerService.getCustomerById(999L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/customers/999"))
				.andExpect(status().isNotFound());

		verify(customerService).getCustomerById(999L);
	}

	@Test
	void getCustomers_shouldReturnAllCustomers() throws Exception {
		List<Customer> customers = List.of(
				customer,
				Customer.builder().id(2L).firstName("Jane").lastName("Smith").build()
		);
		when(customerService.getAllCustomers()).thenReturn(customers);

		mockMvc.perform(get("/api/customers"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].firstName").value("John"))
				.andExpect(jsonPath("$[0].lastName").value("Doe"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].firstName").value("Jane"))
				.andExpect(jsonPath("$[1].lastName").value("Smith"));

		verify(customerService).getAllCustomers();
	}

	@Test
	void getCustomers_shouldReturnEmptyList_whenNoCustomersExist() throws Exception {
		when(customerService.getAllCustomers()).thenReturn(List.of());

		mockMvc.perform(get("/api/customers"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());

		verify(customerService).getAllCustomers();
	}

	@Test
	void createCustomer_shouldReturnCreated() throws Exception {
		Customer newCustomer = Customer.builder()
				.firstName("Alice")
				.lastName("Johnson")
				.build();
		doNothing().when(customerService).createCustomer(any(Customer.class));

		mockMvc.perform(post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newCustomer)))
				.andExpect(status().isCreated());

		verify(customerService).createCustomer(any(Customer.class));
	}

	@Test
	void updateCustomer_shouldReturnOk() throws Exception {
		Customer updatedCustomer = Customer.builder()
				.firstName("John")
				.lastName("Updated")
				.build();
		doNothing().when(customerService).updateCustomer(any(Customer.class));

		mockMvc.perform(put("/api/customers/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedCustomer)))
				.andExpect(status().isOk());

		verify(customerService).updateCustomer(any(Customer.class));
	}

	@Test
	void deleteCustomer_shouldReturnNoContent() throws Exception {
		doNothing().when(customerService).deleteCustomer(1L);

		mockMvc.perform(delete("/api/customers/1"))
				.andExpect(status().isNoContent());

		verify(customerService).deleteCustomer(1L);
	}
}