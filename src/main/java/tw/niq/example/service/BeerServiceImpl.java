package tw.niq.example.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.dto.BeerDto;
import tw.niq.example.mapper.BeerMapper;
import tw.niq.example.repository.BeerRepository;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

	private final BeerRepository beerRepository;
	
	private final BeerMapper beerMapper;
	
	@Override
	public Flux<BeerDto> listBeers() {
		return beerRepository.findAll()
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Mono<BeerDto> getBeerById(Integer beerId) {
		return beerRepository.findById(beerId)
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Mono<BeerDto> createNewBeer(BeerDto beerDto) {
		return beerRepository.save(beerMapper.beerDtoToBeer(beerDto))
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Mono<BeerDto> updateBeer(Integer beerId, BeerDto beerDto) {
		return beerRepository.findById(beerId)
				.map(foundBeer -> {
					foundBeer.setBeerName(beerDto.getBeerName());
					foundBeer.setBeerStyle(beerDto.getBeerStyle());
					foundBeer.setUpc(beerDto.getUpc());
					foundBeer.setQuantityOnHand(beerDto.getQuantityOnHand());
					foundBeer.setPrice(beerDto.getPrice());
					return foundBeer;
				})
				.flatMap(beerRepository::save)
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Mono<BeerDto> patchBeer(Integer beerId, BeerDto beerDto) {
		return beerRepository.findById(beerId)
				.map(foundBeer -> {
					if (StringUtils.hasText(beerDto.getBeerName())) {
						foundBeer.setBeerName(beerDto.getBeerName());
					}
					if (StringUtils.hasText(beerDto.getBeerStyle())) {
						foundBeer.setBeerStyle(beerDto.getBeerStyle());
					}
					if (StringUtils.hasText(beerDto.getUpc())) {
						foundBeer.setUpc(beerDto.getUpc());
					}
					if (beerDto.getQuantityOnHand() != null) {
						foundBeer.setQuantityOnHand(beerDto.getQuantityOnHand());
					}
					if (beerDto.getPrice() != null) {
						foundBeer.setPrice(beerDto.getPrice());
					}
					return foundBeer;
				})
				.flatMap(beerRepository::save)
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Mono<Void> deleteBeerById(Integer beerId) {
		return beerRepository.deleteById(beerId);
	}

}
