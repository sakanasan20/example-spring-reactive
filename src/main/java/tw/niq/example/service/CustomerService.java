package tw.niq.example.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.dto.CustomerDto;

public interface CustomerService {

	Flux<CustomerDto> listCustomers();

	Mono<CustomerDto> getCustomerById(Integer customerId);

	Mono<CustomerDto> createNewCustomer(CustomerDto customerDto);

	Mono<CustomerDto> updateCustomer(Integer customerId, CustomerDto customerDto);

	Mono<CustomerDto> patchCustomer(Integer customerId, CustomerDto customerDto);

	Mono<Void> deleteCustomerById(Integer customerId);
	
}
