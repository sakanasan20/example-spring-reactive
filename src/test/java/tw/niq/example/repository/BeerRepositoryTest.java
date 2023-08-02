package tw.niq.example.repository;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import tw.niq.example.config.DatabaseConfig;
import tw.niq.example.domain.Beer;

@Slf4j
@DataR2dbcTest
@Import(DatabaseConfig.class)
class BeerRepositoryTest {
	
	@Autowired
	BeerRepository beerRepository;
	
	Beer beer;

	@BeforeEach
	void setUp() throws Exception {
		beer = Beer.builder()
				.beerName("Space Dust")
				.beerStyle("IPA")
				.price(BigDecimal.TEN)
				.quantityOnHand(12)
				.upc("123456")
				.build();
	}
	
	@Test
	void testCreateJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		log.info(objectMapper.writeValueAsString(beer));
	}

	@Test
	void testSaveBeer() {
		beerRepository.save(beer)
			.subscribe(beer -> log.info(beer.toString()));
	}

}
