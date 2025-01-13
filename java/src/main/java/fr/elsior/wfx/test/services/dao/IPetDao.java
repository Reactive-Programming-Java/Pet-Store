package fr.elsior.wfx.test.services.dao;

import fr.elsior.wfx.test.exceptions.OwnerNotFoundException;
import fr.elsior.wfx.test.exceptions.PetAlreadyExistsException;
import fr.elsior.wfx.test.exceptions.PetNotFoundException;
import fr.elsior.wfx.test.model.Pet;

import java.util.List;

/**
 * Author: Elimane
 */
public interface IPetDao {
    List<Pet> getPets();

    Pet addPet(Pet pet) throws PetAlreadyExistsException;

    Pet getPet(int id) throws PetNotFoundException;

    List<Pet> getPetsByOwner(String owner) throws OwnerNotFoundException;

    Pet updatePet(int id, Pet updatedPet) throws PetNotFoundException;

    boolean deletePet(int id) throws PetNotFoundException;
}
