package net.groundgurus.customer_api_jdbc.service;

import net.groundgurus.customer_api_jdbc.model.Customer;
import net.groundgurus.customer_api_jdbc.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void getCustomerById_shouldReturnCustomer_whenCustomerExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerById(1L);

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_shouldReturnEmpty_whenCustomerDoesNotExist() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.getCustomerById(999L);

        assertTrue(result.isEmpty());
        verify(customerRepository).findById(999L);
    }

    @Test
    void getAllCustomers_shouldReturnAllCustomers() {
        List<Customer> customers = List.of(
                customer,
                Customer.builder().id(2L).firstName("Jane").lastName("Smith").build()
        );
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals(customers, result);
        verify(customerRepository).findAll();
    }

    @Test
    void getAllCustomers_shouldReturnEmptyList_whenNoCustomersExist() {
        when(customerRepository.findAll()).thenReturn(List.of());

        List<Customer> result = customerService.getAllCustomers();

        assertTrue(result.isEmpty());
        verify(customerRepository).findAll();
    }

    @Test
    void createCustomer_shouldSaveCustomer() {
        Customer newCustomer = Customer.builder()
                .firstName("Alice")
                .lastName("Johnson")
                .build();

        customerService.createCustomer(newCustomer);

        verify(customerRepository).save(newCustomer);
    }

    @Test
    void updateCustomer_shouldUpdateExistingCustomer() {
        Customer updatedCustomer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Updated")
                .build();

        customerService.updateCustomer(updatedCustomer);

        verify(customerRepository).save(updatedCustomer);
    }

    @Test
    void deleteCustomer_shouldDeleteCustomer() {
        long customerId = 1L;

        customerService.deleteCustomer(customerId);

        verify(customerRepository).deleteById(customerId);
    }
}