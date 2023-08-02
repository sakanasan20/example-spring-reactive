package tw.niq.example.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import tw.niq.example.domain.Person;

@Slf4j
class PersonRepositoryImplTest {
	
	PersonRepository personRepository = new PersonRepositoryImpl();

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGetById_block() {
		
		Mono<Person> monoPerson = personRepository.getById(1);
		
		// blocking IO
		Person person = monoPerson.block();
		
		log.info(person.toString());
	}
	
	@Test
	void testGetById_subscribe() {
		
		Mono<Person> monoPerson = personRepository.getById(1);
		
		// non-blocking IO
		monoPerson.subscribe(person -> log.info(person.toString()));
	}

	@Test
	void testGetById_map_subscribe() {
		
		Mono<Person> monoPerson = personRepository.getById(1);
		
		monoPerson
			.map(person -> person.getFirstName() + " " + person.getLastName())
			.subscribe(fullName -> log.info(fullName));
	}
	
	@Test
	void testGetById_whenIdFound() {
		
		Mono<Person> monoPerson = personRepository.getById(1);
		
		assertTrue(monoPerson.hasElement().block());
	}
	
	@Test
	void testGetById_whenIdNotFound() {
		
		Mono<Person> monoPerson = personRepository.getById(5);
		
		assertFalse(monoPerson.hasElement().block());
	}
	
	@Test
	void testGetById_whenIdFound_stepVerifier() {
		
		Mono<Person> monoPerson = personRepository.getById(1);
		
		StepVerifier.create(monoPerson).expectNextCount(1).verifyComplete();
		
		monoPerson.subscribe(person -> log.info(person.toString()));
	}
	
	@Test
	void testGetById_whenIdNotFound_stepVerifier() {
		
		Mono<Person> monoPerson = personRepository.getById(5);
		
		StepVerifier.create(monoPerson).expectNextCount(0).verifyComplete();
		
		monoPerson.subscribe(person -> log.info(person.toString()));
	}
	
	@Test
	void testGetAll_blockFirst() {
		
		Flux<Person> fluxPerson = personRepository.getAll();
		
		// blocking IO
		Person person = fluxPerson.blockFirst();
		
		log.info(person.toString());
	}
	
	@Test
	void testGetAll_map_subscribe() {
		
		Flux<Person> fluxPerson = personRepository.getAll();
		
		fluxPerson
			.map(person -> person.getFirstName() + " " + person.getLastName())
			.subscribe(fullName -> log.info(fullName));
	}

	@Test
	void testGetAll_collectList() {
		
		Flux<Person> fluxPerson = personRepository.getAll();
		
		Mono<List<Person>> monoListPerson = fluxPerson.collectList();
		
		monoListPerson.subscribe(listPerson -> listPerson.forEach(person -> log.info(person.toString())));
	}
	
	@Test
	void testGetAll_filter_subscribe() {
		
		personRepository.getAll()
			.filter(person -> person.getId().equals(1))
			.subscribe(person -> log.info(person.toString()));
	}
	
	@Test
	void testGetAll_filter_next() {
		
		Mono<Person> monoPerson = personRepository.getAll()
				.filter(person -> person.getId().equals(1))
				.next();
		
		monoPerson
			.map(person -> person.getFirstName() + " " + person.getLastName())
			.subscribe(fullName -> log.info(fullName));
	}
	
	@Test
	void testGetAll_filter_single_doOnError() {
		
		Flux<Person> fluxPerson = personRepository.getAll();
		
		Mono<Person> monoPerson = fluxPerson
				.filter(person -> person.getId().equals(5))
				.single()
				.doOnError(throwable -> log.info("Error in flux: " + throwable.toString())); // will not show error if not subscribe
		
		monoPerson.subscribe(
				person -> log.info(person.toString()), 
				throwable -> log.info("Error in mono: " + throwable.toString()) // will not show error if not subscribe
		);
	}
	
}
