package fr.elsior.wfx.test.services;

import fr.elsior.wfx.test.exceptions.OwnerNotFoundException;
import fr.elsior.wfx.test.exceptions.PetAlreadyExistsException;
import fr.elsior.wfx.test.exceptions.PetNotFoundException;
import fr.elsior.wfx.test.model.Pet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Author: Elimane
 */
public interface IPetService {
    Flux<Pet> getPets();

    Mono<Pet> addPet(Pet pet) throws PetAlreadyExistsException;

    Mono<Pet> getPet(int id) throws PetNotFoundException;

    Flux<Pet> getPetsByOwner(String owner) throws OwnerNotFoundException;

    Mono<Pet> updatePet(int id, Pet updatedPet) throws PetNotFoundException;

    void deletePet(int id) throws PetNotFoundException;
}
