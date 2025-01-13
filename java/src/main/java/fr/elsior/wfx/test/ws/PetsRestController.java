package fr.elsior.wfx.test.ws;

import fr.elsior.wfx.test.model.Pet;
import fr.elsior.wfx.test.services.IPetService;
import fr.elsior.wfx.test.exceptions.OwnerNotFoundException;
import fr.elsior.wfx.test.exceptions.PetAlreadyExistsException;
import fr.elsior.wfx.test.exceptions.PetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Elimane
 */
@RestController
@RequestMapping("/pets")
public class PetsRestController {

    private final IPetService petService;

    /**
     * Constructor with dependency injection.
     *
     * @param petService The service for handling pets.
     */
    public PetsRestController(IPetService petService) {
        this.petService = petService;
    }

    /**
     * Retrieves all pets.
     *
     * @return A reactive stream (Flux) of pets with HTTP status 200 (OK).
     */
    @GetMapping
    public Flux<Pet> listPets() {
        return petService.getPets();
    }

    /**
     * Adds a new pet.
     *
     * @param pet The pet to add.
     * @return A reactive Mono with the added pet and HTTP status 201 (Created).
     * @throws PetAlreadyExistsException If a pet with the same ID already exists.
     */
    @PostMapping(value = "/add")
    public Mono<ResponseEntity<Pet>> addPet(@RequestBody Pet pet) throws PetAlreadyExistsException {
        return petService.addPet(pet)
                .map(savedPet -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON) // Fixe le Content-Type
                        .body(savedPet))
                .onErrorResume(PetAlreadyExistsException.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()));
    }


    /**
     * Retrieves a pet by its ID.
     *
     * @param id The ID of the pet.
     * @return A reactive Mono with the requested pet and HTTP status 200 (OK).
     * @throws PetNotFoundException If no pet is found with the given ID.
     */
    @GetMapping(value = "/{id}")
    public Mono<Pet> getPet(@PathVariable int id) throws PetNotFoundException {
        return petService.getPet(id);
    }


    /**
     * Retrieves all pets owned by a specific owner.
     *
     * @param owner The name of the owner.
     * @return A reactive stream (Flux) of pets owned by the specified owner.
     * @throws OwnerNotFoundException If no pets are found for the given owner.
     */
    @GetMapping("/owner/{owner}")
    public Flux<Pet> getPetsByOwner(@PathVariable String owner) throws OwnerNotFoundException {
        return petService.getPetsByOwner(owner);
    }

    /**
     * Updates an existing pet.
     *
     * @param id         The ID of the pet to update.
     * @param updatedPet The updated pet details.
     * @return A reactive Mono with the updated pet and HTTP status 200 (OK).
     * @throws PetNotFoundException If no pet is found with the given ID.
     */
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Pet>> updatePet(@PathVariable int id, @RequestBody Pet updatedPet) throws PetNotFoundException {
        return petService.updatePet(id, updatedPet)
                .map(pet -> ResponseEntity.ok().body(pet))
                .onErrorResume(PetNotFoundException.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    /**
     * Deletes a pet by its ID.
     *
     * @param id The ID of the pet to delete.
     * @return HTTP status 204 (No Content) if the deletion is successful.
     * @throws PetNotFoundException If no pet is found with the given ID.
     */
    @DeleteMapping("remove/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable int id) throws PetNotFoundException {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Global exception handler for all exceptions.
     *
     * @param ex The captured exception.
     * @return An error message and appropriate HTTP status code.
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleExceptions(Exception ex) {
        if (ex instanceof PetNotFoundException) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
        } else if (ex instanceof PetAlreadyExistsException) {
            return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()));
        } else if (ex instanceof OwnerNotFoundException) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
        }
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error: " + ex.getMessage()));
    }
}
