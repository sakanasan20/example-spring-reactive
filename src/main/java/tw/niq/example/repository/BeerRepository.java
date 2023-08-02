package tw.niq.example.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import tw.niq.example.domain.Beer;

public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {

}
