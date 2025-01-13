package fr.elsior.wfx.test.services;

import fr.elsior.wfx.test.services.dao.IPetDao;
import fr.elsior.wfx.test.exceptions.OwnerNotFoundException;
import fr.elsior.wfx.test.exceptions.PetAlreadyExistsException;
import fr.elsior.wfx.test.exceptions.PetNotFoundException;
import fr.elsior.wfx.test.model.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Author: Elimane
 */
@Service
public class PetServiceImpl implements IPetService {

    private static final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);
    final IPetDao petDao;

    public PetServiceImpl(IPetDao petDao) {
        this.petDao = petDao;
    }

    /**
     * Retrieves all pets.
     *
     * @return A reactive stream (Flux) of all pets.
     */
    @Override
    public Flux<Pet> getPets() {
        logger.info("Fetching all pets");
        return Mono.fromCallable(petDao::getPets)
                .flatMapMany(Flux::fromIterable);
    }

    /**
     * Adds a new pet.
     *
     * @param pet The pet to add.
     * @return A Mono containing the added pet.
     */
    @Override
    public Mono<Pet> addPet(Pet pet) {
        logger.info("Adding a new pet: {}", pet);
        return Mono.fromCallable(() -> petDao.addPet(pet))
                .handle((addedPet, sink) -> {
                    if (addedPet == null) {
                        logger.error("Pet with ID {} already exists", pet.getId());
                        sink.error(new PetAlreadyExistsException());
                    } else {
                        logger.debug("Pet added successfully: {}", addedPet);
                        sink.next(addedPet);
                    }
                });
    }

    /**
     * Retrieves a pet by its ID.
     *
     * @param id The ID of the pet to retrieve.
     * @return A Mono containing the pet with the given ID.
     */
    @Override
    public Mono<Pet> getPet(int id) {
        logger.info("Fetching pet with ID {}", id);
        return Mono.fromCallable(() -> petDao.getPet(id))
                .handle((pet, sink) -> {
                    if (pet == null) {
                        logger.error("No pet found with ID {}", id);
                        sink.error(new PetNotFoundException());
                    } else {
                        sink.next(pet);
                    }
                });
    }

    /**
     * Retrieves all pets owned by a specific owner.
     *
     * @param owner The name of the owner.
     * @return A reactive stream (Flux) of pets owned by the specified owner.
     */
    @Override
    public Flux<Pet> getPetsByOwner(String owner) {
        logger.info("Fetching pets for owner: {}", owner);
        return Mono.fromCallable(() -> petDao.getPetsByOwner(owner))
                .flatMapMany(pets -> pets.isEmpty() ?
                        Flux.error(new OwnerNotFoundException()) :
                        Flux.fromIterable(pets))
                .doOnError(e -> logger.error("No pets found for owner: {}", owner));
    }

    /**
     * Updates an existing pet.
     *
     * @param id         The ID of the pet to update.
     * @param updatedPet The updated pet details.
     * @return A Mono containing the updated pet.
     */
    @Override
    public Mono<Pet> updatePet(int id, Pet updatedPet) {
        logger.info("Updating pet with ID {}", id);
        return Mono.fromCallable(() -> petDao.updatePet(id, updatedPet))
                .handle((pet, sink) -> {
                    if (pet == null) {
                        logger.error("No pet found with ID {}", id);
                        sink.error(new PetNotFoundException());
                    } else {
                        logger.debug("Updated pet details: {}", pet);
                        sink.next(pet);
                    }
                });
    }

    /**
     * Deletes a pet by its ID.
     *
     * @param id The ID of the pet to delete.
     * @throws PetNotFoundException If the pet is not found.
     */
    @Override
    public void deletePet(int id) throws PetNotFoundException {
        logger.info("Deleting pet with ID {}", id);
        boolean removed = petDao.deletePet(id);
        if (!removed) {
            logger.error("No pet found with ID {}", id);
            throw new PetNotFoundException();
        }
        logger.debug("Pet with ID {} deleted successfully", id);
    }
}
