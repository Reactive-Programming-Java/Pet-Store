package fr.elsior.wfx.test.ws;

import fr.elsior.wfx.test.services.IPetService;
import fr.elsior.wfx.test.exceptions.OwnerNotFoundException;
import fr.elsior.wfx.test.exceptions.PetAlreadyExistsException;
import fr.elsior.wfx.test.exceptions.PetNotFoundException;
import fr.elsior.wfx.test.model.Pet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author: Elimane
 */
@WebFluxTest(PetsRestController.class)
class PetsRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IPetService petService;

    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        pet1 = new Pet(1, "Rex", 5, "Jack");
        pet2 = new Pet(2, "Bella", 3, "Alice");
    }

    /**
     * Test to verify that the API returns all pets.
     */
    @Test
    void listPets_ShouldReturnAllPets() {
        List<Pet> pets = Arrays.asList(pet1, pet2);
        Mockito.when(petService.getPets()).thenReturn(Flux.fromIterable(pets));

        webTestClient.get().uri("/pets")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Pet.class)
                .hasSize(2);
    }

    /**
     * Test to verify that the API adds a new pet.
     */
    @Test
    void addPet_ShouldReturnCreatedPet() throws PetAlreadyExistsException {
        Mockito.when(petService.addPet(Mockito.any())).thenReturn(Mono.just(pet1));

        webTestClient.post().uri("/pets/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(pet1)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Pet.class)
                .value(pet -> pet.getName().equals("Rex"));
    }

    /**
     * Test to verify that the API returns a conflict if the pet already exists.
     */
    @Test
    void addPet_ShouldReturnConflict_WhenPetAlreadyExists() throws PetAlreadyExistsException {
        Mockito.when(petService.addPet(Mockito.any()))
                .thenReturn(Mono.error(new PetAlreadyExistsException()));

        webTestClient.post().uri("/pets/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(pet1)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    /**
     * Test to verify that the API returns a pet by its ID.
     */
    @Test
    void getPet_ShouldReturnPetById() throws PetNotFoundException {
        Mockito.when(petService.getPet(1)).thenReturn(Mono.just(pet1));

        webTestClient.get().uri("/pets/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody(Pet.class)
                .value(pet -> assertEquals("Rex", pet.getName()));
    }

    /**
     * Test to verify that the API returns a 404 error if the pet does not exist.
     */
    @Test
    void getPet_ShouldReturnNotFound_WhenPetDoesNotExist() throws PetNotFoundException {
        Mockito.when(petService.getPet(9))
                .thenReturn(Mono.error(new PetNotFoundException()));

        webTestClient.get().uri("/pets/9")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("Pet not found.");
    }

    /**
     * Test to verify that the API returns pets owned by a specific owner.
     */
    @Test
    void getPetsByOwner_ShouldReturnPetsByOwner() throws OwnerNotFoundException {
        List<Pet> pets = Arrays.asList(pet1);
        Mockito.when(petService.getPetsByOwner("Jack")).thenReturn(Flux.fromIterable(pets));

        webTestClient.get().uri("/pets/owner/Jack")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Pet.class)
                .hasSize(1);
    }

    /**
     * Test to verify that the API returns a 404 error if the owner does not exist.
     */
    @Test
    void getPetsByOwner_ShouldReturnNotFound_WhenOwnerDoesNotExist() throws OwnerNotFoundException {
        Mockito.when(petService.getPetsByOwner("Unknown"))
                .thenReturn(Flux.error(new OwnerNotFoundException()));

        webTestClient.get().uri("/pets/owner/Unknown")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("Owner not found.");
    }

    /**
     * Test to verify that the API updates an existing pet.
     */
    @Test
    void updatePet_ShouldUpdateExistingPet() throws PetNotFoundException {
        Pet updatedPet = new Pet(1, "Rex", 6, "Jack");
        Mockito.when(petService.updatePet(Mockito.eq(1), Mockito.any()))
                .thenReturn(Mono.just(updatedPet));

        webTestClient.put().uri("/pets/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedPet)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Pet.class)
                .value(pet -> Assertions.assertEquals(6, pet.getAge()));
    }

    /**
     * Test to verify that the API deletes a pet.
     */
    @Test
    void deletePet_ShouldReturnNoContent() throws PetNotFoundException {
        Mockito.doNothing().when(petService).deletePet(1);

        webTestClient.delete().uri("/pets/remove/1")
                .exchange()
                .expectStatus().isNoContent();
    }


    /**
     * Test to verify that the API returns a 404 error when trying to delete a nonexistent pet.
     */
    @Test
    void deletePet_ShouldReturnNotFound_WhenPetDoesNotExist() throws PetNotFoundException {
        Mockito.doThrow(new PetNotFoundException()).when(petService).deletePet(1);

        webTestClient.delete().uri("/pets/remove/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("Pet not found.");
    }

}
