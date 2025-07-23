package net.groundgurus.customer_api_jdbc.controller;

import lombok.RequiredArgsConstructor;
import net.groundgurus.customer_api_jdbc.model.Customer;
import net.groundgurus.customer_api_jdbc.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	private final CustomerService customerService;

	@GetMapping("{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable long id) {
		var customer = customerService.getCustomerById(id);
		return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping
	public ResponseEntity<List<Customer>> getCustomers() {
		var customers = customerService.getAllCustomers();
		return !customers.isEmpty() ? ResponseEntity.ok(customers) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
	}

	@PostMapping
	public ResponseEntity<Void> createCustomer(@RequestBody Customer customer) {
		customerService.createCustomer(customer);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("{id}")
	public ResponseEntity<Void> updateCustomer(@PathVariable long id, @RequestBody Customer customer) {
		customer.setId(id);
		customerService.updateCustomer(customer);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {
		customerService.deleteCustomer(id);
		return ResponseEntity.ok().build();
	}
}
