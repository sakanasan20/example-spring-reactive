package tw.niq.example.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.domain.Person;

public interface PersonRepository {

	Mono<Person> getById(Integer id);
	
	Flux<Person> getAll();
	
}
