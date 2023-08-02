package tw.niq.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.dto.CustomerDto;
import tw.niq.example.service.CustomerService;

@RequiredArgsConstructor
@RestController
public class CustomerController {

	public static final String CUSTOMER_PATH = "/api/v2/customers";
	
	public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
	
	private final CustomerService customerService;

	@GetMapping(CUSTOMER_PATH)
	Flux<CustomerDto> listCustomers() {
		return customerService.listCustomers();
	}
	
	@GetMapping(CUSTOMER_PATH_ID)
	Mono<CustomerDto> getCustomerById(@PathVariable("customerId") Integer customerId) {
		return customerService.getCustomerById(customerId);
	}
	
	@PostMapping(CUSTOMER_PATH)
	Mono<ResponseEntity<Void>> createNewCustomer(@Validated @RequestBody CustomerDto customerDto) {
		return customerService.createNewCustomer(customerDto)
				.map(savedDto -> ResponseEntity.created(UriComponentsBuilder
						.fromHttpUrl("http://localhost:8080/" + CUSTOMER_PATH + "/" + savedDto.getId()).build().toUri())
						.build());
	}
	
	@PostMapping(CUSTOMER_PATH_ID)
	Mono<ResponseEntity<Void>> updateCustomer(@PathVariable("customerId") Integer customerId, @Validated @RequestBody CustomerDto customerDto) {
		return customerService.updateCustomer(customerId, customerDto)
				.map(updatedCustomer -> ResponseEntity.ok().build());
	}
	
	@PatchMapping(CUSTOMER_PATH_ID)
	Mono<ResponseEntity<Void>> patchCustomer(@PathVariable("customerId") Integer customerId, @Validated @RequestBody CustomerDto customerDto) {
		return customerService.patchCustomer(customerId, customerDto)
				.map(patchedCustomer -> ResponseEntity.ok().build());
	}
	
	@DeleteMapping(CUSTOMER_PATH_ID)
	Mono<ResponseEntity<Void>> deleteCustomerById(@PathVariable("customerId") Integer customerId) {
		return customerService.deleteCustomerById(customerId).map(response ->  ResponseEntity.noContent().build());
	}
	
}
