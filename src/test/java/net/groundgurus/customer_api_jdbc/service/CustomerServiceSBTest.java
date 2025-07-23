package net.groundgurus.customer_api_jdbc.service;

import net.groundgurus.customer_api_jdbc.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerServiceSBTest {

	@Autowired
	private CustomerService customerService;

	@Test
	void testCreateCustomer() {
		var newCustomer = Customer.builder()
				.firstName("John")
				.lastName("Doe")
				.build();
		customerService.createCustomer(newCustomer);

		var customer = customerService.getCustomerById(1);
		assertNotNull(customer);
		assertTrue(customer.isPresent());
		assertEquals("John", customer.get().getFirstName());
		assertEquals("Doe", customer.get().getLastName());
	}

	@Test
	void testGetAllCustomers() {
		createCustomer("John", "Doe");
		createCustomer("Jane", "Smith");

		var customers = customerService.getAllCustomers();
		assertNotNull(customers);
		assertFalse(customers.isEmpty());
		assertEquals(2, customers.size());
	}

	@Test
	void testDeleteCustomer() {
		createCustomer("John", "Doe");

		customerService.deleteCustomer(1);

		var customer = customerService.getCustomerById(1);

		assertNotNull(customer);
		assertFalse(customer.isPresent());
	}

	@Test
	void testUpdateCustomer() {
		createCustomer("John", "Doe");

		var customerOpt = customerService.getCustomerById(1);
		assertTrue(customerOpt.isPresent());

		var customer = customerOpt.get();
		customer.setFirstName("Joseph");
		customer.setLastName("Smith");

		customerService.updateCustomer(customer);

		customerOpt = customerService.getCustomerById(1);

		assertTrue(customerOpt.isPresent());
		assertEquals("Joseph", customerOpt.get().getFirstName());
		assertEquals("Smith", customerOpt.get().getLastName());
	}

	private void createCustomer(String firstName, String lastName) {
		var customer = Customer.builder()
				.firstName(firstName)
				.lastName(lastName)
				.build();
		customerService.createCustomer(customer);
	}
}
