package tw.niq.example.controller;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

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
import tw.niq.example.domain.Beer;
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
		webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerController.BEER_PATH)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-type", "application/json")
			.expectBody().jsonPath("$.size()").isEqualTo(3);
	}

	@Order(2)
	@Test
	void testGetBeerById() {
		webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerController.BEER_PATH_ID, 1)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-type", "application/json")
			.expectBody(BeerDto.class);
	}
	
	@Order(2)
	@Test
	void testGetBeerById_whenIdNotExist() {
		webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerController.BEER_PATH_ID, 999)
			.exchange()
			.expectStatus().isNotFound();
	}

	@Order(3)
	@Test
	void testCreateNewBeer() {
		webTestClient.mutateWith(mockOAuth2Login()).post().uri(BeerController.BEER_PATH)
			.body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
			.header("Content-type", "application/json")
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().location("http://localhost:8080/api/v2/beers/4");
	}
	
	@Order(3)
	@Test
	void testCreateNewBeer_withInvalidBeer() {
		
		Beer invalidBeer = BeerRepositoryTest.getTestBeer();
		invalidBeer.setBeerName("");
		
		webTestClient.mutateWith(mockOAuth2Login()).post().uri(BeerController.BEER_PATH)
			.body(Mono.just(invalidBeer), BeerDto.class)
			.header("Content-type", "application/json")
			.exchange()
			.expectStatus().isBadRequest();
	}

	@Order(4)
	@Test
	void testUpdateBeer() {
		webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerController.BEER_PATH_ID, 1)
			.body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
			.exchange()
			.expectStatus().isNoContent();
	}
	
	@Order(4)
	@Test
	void testUpdateBeer_withInvalidBeer() {
		
		Beer invalidBeer = BeerRepositoryTest.getTestBeer();
		invalidBeer.setBeerName("");
		
		webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerController.BEER_PATH_ID, 1)
			.body(Mono.just(invalidBeer), BeerDto.class)
			.exchange()
			.expectStatus().isBadRequest();
	}
	
	@Order(4)
	@Test
	void testUpdateBeer_whenIdNotFound() {
		webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerController.BEER_PATH_ID, 999)
		.body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
		.exchange()
		.expectStatus().isNotFound();
	}

	@Order(5)
	@Test
	void testPatchBeer() {
		webTestClient.mutateWith(mockOAuth2Login()).patch().uri(BeerController.BEER_PATH_ID, 1)
			.body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
			.exchange()
			.expectStatus().isNoContent();
	}
	
	@Order(5)
	@Test
	void testPatchBeer_withInvalidBeer() {
		Beer invalidBeer = BeerRepositoryTest.getTestBeer();
		invalidBeer.setBeerName("");
		
		webTestClient.mutateWith(mockOAuth2Login()).patch().uri(BeerController.BEER_PATH_ID, 1)
			.body(Mono.just(invalidBeer), BeerDto.class)
			.exchange()
			.expectStatus().isBadRequest();
	}
	
	@Order(5)
	@Test
	void testPatchBeer_whenIdNotFound() {
		webTestClient.mutateWith(mockOAuth2Login()).patch().uri(BeerController.BEER_PATH_ID, 999)
			.body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
			.exchange()
			.expectStatus().isNotFound();
	}

	@Order(999)
	@Test
	void testDeleteBeerById() {
		webTestClient.mutateWith(mockOAuth2Login()).delete().uri(BeerController.BEER_PATH_ID, 1)
			.exchange()
			.expectStatus().isNoContent();
	}
	
	@Order(999)
	@Test
	void testDeleteBeerById_whenIdNotFound() {
		webTestClient.mutateWith(mockOAuth2Login()).delete().uri(BeerController.BEER_PATH_ID, 999)
			.exchange()
			.expectStatus().isNotFound();
	}

}
