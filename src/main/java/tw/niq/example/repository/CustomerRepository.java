package tw.niq.example.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import tw.niq.example.domain.Customer;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

}
