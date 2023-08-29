package tw.niq.example.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;
import tw.niq.example.dto.BeerDto;
import tw.niq.example.repository.BeerRepositoryTest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {
	
	@Autowired
	WebTestClient webTestClient;

	@BeforeEach
	void setUp() throws Exception {
	}
	
	@Order(1)
	@Test
	void testListBeers() {
		webTestClient.get().uri(BeerController.BEER_PATH)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-type", "application/json")
			.expectBody().jsonPath("$.size()").isEqualTo(3);
	}

	@Order(2)
	@Test
	void testGetBeerById() {
		webTestClient.get().uri(BeerController.BEER_PATH_ID, 1)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-type", "application/json")
			.expectBody(BeerDto.class);
	}

	@Order(3)
	@Test
	void testCreateNewBeer() {
		webTestClient.post().uri(BeerController.BEER_PATH)
			.body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
			.header("Content-type", "application/json")
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().location("http://localhost:8080/api/v2/beers/4");
	}

	@Order(4)
	@Test
	void testUpdateBeer() {
		webTestClient.put().uri(BeerController.BEER_PATH_ID, 1)
			.body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
			.exchange()
			.expectStatus().isNoContent();
	}

	@Order(5)
	@Test
	void testPatchBeer() {
		webTestClient.patch().uri(BeerController.BEER_PATH_ID, 1)
			.body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
			.exchange()
			.expectStatus().isNoContent();
	}

	@Order(999)
	@Test
	void testDeleteBeerById() {
		webTestClient.delete().uri(BeerController.BEER_PATH_ID, 1)
			.exchange()
			.expectStatus().isNoContent();
	}

}
