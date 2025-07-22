package net.groundgurus.customer_api_jdbc.repository;

import net.groundgurus.customer_api_jdbc.model.Customer;
import org.springframework.data.repository.ListCrudRepository;

public interface CustomerRepository extends ListCrudRepository<Customer, Long> {
}
