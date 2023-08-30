package tw.niq.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.dto.BeerDto;
import tw.niq.example.service.BeerService;

@RequiredArgsConstructor
@RestController
public class BeerController {

	public static final String BEER_PATH = "/api/v2/beers";
	
	public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
	
	private final BeerService beerService;

	@GetMapping(BEER_PATH)
	Flux<BeerDto> listBeers() {
		return beerService.listBeers();
	}
	
	@GetMapping(BEER_PATH_ID)
	Mono<BeerDto> getBeerById(@PathVariable("beerId") Integer beerId) {
		return beerService.getBeerById(beerId)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
	}
	
	@PostMapping(BEER_PATH)
	Mono<ResponseEntity<Void>> createNewBeer(@Validated @RequestBody BeerDto beerDto) {
		return beerService.createNewBeer(beerDto)
				.map(savedDto -> ResponseEntity.created(UriComponentsBuilder
						.fromHttpUrl("http://localhost:8080/" + BEER_PATH + "/" + savedDto.getId()).build().toUri())
						.build());
	}
	
	@PutMapping(BEER_PATH_ID)
	Mono<ResponseEntity<Void>> updateBeer(@PathVariable("beerId") Integer beerId, @Validated @RequestBody BeerDto beerDto) {
		return beerService.updateBeer(beerId, beerDto)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
				.map(updatedBeer -> ResponseEntity.noContent().build());
	}
	
	@PatchMapping(BEER_PATH_ID)
	Mono<ResponseEntity<Void>> patchBeer(@PathVariable("beerId") Integer beerId, @Validated @RequestBody BeerDto beerDto) {
		return beerService.patchBeer(beerId, beerDto)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
				.map(patchedBeer -> ResponseEntity.noContent().build());
	}
	
	@DeleteMapping(BEER_PATH_ID)
	Mono<ResponseEntity<Void>> deleteBeerById(@PathVariable("beerId") Integer beerId) {
		return beerService.getBeerById(beerId)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
				.map(beerDto -> beerService.deleteBeerById(beerDto.getId()))
				.thenReturn(ResponseEntity.noContent().build());
	}
	
}
