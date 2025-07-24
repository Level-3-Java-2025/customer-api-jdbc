package net.groundgurus.customer_api_jdbc.controller;

import net.groundgurus.customer_api_jdbc.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerControllerSBTest {

	@Autowired
	private TestRestTemplate restTemplate;

	private static final String CUSTOMER_API = "/api/customers";

	@Test
	void testCreateCustomer() {
		var customer = Customer.builder()
				.firstName("John")
				.lastName("Doe")
				.build();

		ResponseEntity<Void> response = restTemplate.postForEntity(CUSTOMER_API, customer, Void.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void testFindAllCustomers() {
		createCustomer("John", "Doe");
		createCustomer("Jane", "Smith");

		var response = restTemplate.exchange(CUSTOMER_API, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Customer>>() {});

		List<Customer> body = response.getBody();
		assertNotNull(body);
		assertFalse(body.isEmpty());
		assertEquals(2, body.size());
		assertEquals(HttpStatus.OK, response.getStatusCode());

		Optional<Customer> john = body.stream()
				.filter(customer -> customer.getFirstName().equals("John") && customer.getLastName().equals("Doe"))
				.findFirst();
		assertTrue(john.isPresent());
	}

	@Test
	void testFindAllCustomersNoCustomers() {
		var response = restTemplate.exchange(CUSTOMER_API, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Customer>>() {});
		List<Customer> body = response.getBody();
		assertNotNull(body);
		assertTrue(body.isEmpty());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testFindCustomerById() {
		createCustomer("John", "Doe");
		ResponseEntity<Customer> response = restTemplate.getForEntity(CUSTOMER_API + "/1", Customer.class);

		var customer = response.getBody();

		assertNotNull(customer);
		assertEquals("John", customer.getFirstName());
		assertEquals("Doe", customer.getLastName());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testUpdateCustomer() {
		createCustomer("John", "Doe");

		var customer = Customer.builder()
				.id(1)
				.firstName("Joseph")
				.lastName("Smith")
				.build();
		ResponseEntity<Void> response = restTemplate.exchange(CUSTOMER_API + "/1",
				HttpMethod.PUT,
				new HttpEntity<>(customer),
				Void.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		var getResponse = restTemplate.getForEntity(CUSTOMER_API + "/1", Customer.class);
		assertNotNull(getResponse.getBody());
		assertEquals("Joseph", getResponse.getBody().getFirstName());
		assertEquals("Smith", getResponse.getBody().getLastName());
	}

	@Test
	void testDeleteCustomer() {
		createCustomer("John", "Doe");
		var response = restTemplate.exchange(
				CUSTOMER_API + "/1",
				HttpMethod.DELETE,
				null,
				Void.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	private void createCustomer(String firstName, String lastName) {
		var customer = Customer.builder()
				.firstName(firstName)
				.lastName(lastName)
				.build();

		restTemplate.postForEntity("/api/customers", customer, Void.class);
	}
}
