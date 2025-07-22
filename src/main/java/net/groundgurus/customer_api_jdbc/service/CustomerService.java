package net.groundgurus.customer_api_jdbc.service;

import lombok.RequiredArgsConstructor;
import net.groundgurus.customer_api_jdbc.model.Customer;
import net.groundgurus.customer_api_jdbc.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;

	public Optional<Customer> getCustomerById(long id) {
		return customerRepository.findById(id);
	}

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public void createCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	public void deleteCustomer(long id) {
		customerRepository.deleteById(id);
	}

	public void updateCustomer(Customer customer) {
		customerRepository.save(customer);
	}
}
