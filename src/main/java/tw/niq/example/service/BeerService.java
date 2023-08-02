package tw.niq.example.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.dto.BeerDto;

public interface BeerService {

	Flux<BeerDto> listBeers();

	Mono<BeerDto> getBeerById(Integer beerId);

	Mono<BeerDto> createNewBeer(BeerDto beerDto);

	Mono<BeerDto> updateBeer(Integer beerId, BeerDto beerDto);

	Mono<BeerDto> patchBeer(Integer beerId, BeerDto beerDto);

	Mono<Void> deleteBeerById(Integer beerId);
	
}
