package fr.elsior.wfx.test.services.dao;

import fr.elsior.wfx.test.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author: Elimane
 */
import static org.junit.jupiter.api.Assertions.*;

class PetDaoImplTest {

    private PetDaoImpl petDao;

    @BeforeEach
    void setUp() {
        petDao = new PetDaoImpl();
        petDao.initPetList();
    }

    /**
     * Test to verify that all pets are retrieved successfully.
     */
    @Test
    void getPets_ShouldReturnAllPets() {
        List<Pet> pets = petDao.getPets();
        assertEquals(6, pets.size());
    }

    /**
     * Test to verify that a new pet is added successfully.
     */
    @Test
    void addPet_ShouldAddNewPet() {
        Pet newPet = new Pet(7, "Buddy", 3, "Alice");
        Pet addedPet = petDao.addPet(newPet);

        assertNotNull(addedPet);
        assertEquals(7, addedPet.getId());
        assertEquals(7, petDao.getPets().size());
    }

    /**
     * Test to verify that adding a pet with an existing ID returns null.
     */
    @Test
    void addPet_ShouldReturnNull_WhenPetAlreadyExists() {
        Pet duplicatePet = new Pet(1, "Duplicate", 4, "John");
        Pet result = petDao.addPet(duplicatePet);

        assertNull(result);
        assertEquals(6, petDao.getPets().size());
    }

    /**
     * Test to verify that a pet is retrieved by its ID.
     */
    @Test
    void getPet_ShouldReturnPetById() {
        Pet pet = petDao.getPet(1);

        assertNotNull(pet);
        assertEquals("Medor", pet.getName());
    }

    /**
     * Test to verify that retrieving a non-existing pet by ID returns null.
     */
    @Test
    void getPet_ShouldReturnNull_WhenPetNotFound() {
        Pet pet = petDao.getPet(10);

        assertNull(pet);
    }

    /**
     * Test to verify that pets are retrieved by their owner.
     */
    @Test
    void getPetsByOwner_ShouldReturnPetsByOwner() {
        List<Pet> pets = petDao.getPetsByOwner("Sarah");

        assertEquals(3, pets.size());
    }

    /**
     * Test to verify that retrieving pets for a non-existing owner returns an empty list.
     */
    @Test
    void getPetsByOwner_ShouldReturnEmptyList_WhenOwnerNotFound() {
        List<Pet> pets = petDao.getPetsByOwner("Unknown");

        assertTrue(pets.isEmpty());
    }

    /**
     * Test to verify that a pet is updated successfully.
     */
    @Test
    void updatePet_ShouldUpdateExistingPet() {
        Pet updatedPet = new Pet(1, "UpdatedName", 10, "UpdatedOwner");
        Pet result = petDao.updatePet(1, updatedPet);

        assertNotNull(result);
        assertEquals("UpdatedName", result.getName());
        assertEquals(10, result.getAge());
        assertEquals("UpdatedOwner", result.getOwner());
    }

    /**
     * Test to verify that updating a non-existing pet returns null.
     */
    @Test
    void updatePet_ShouldReturnNull_WhenPetNotFound() {
        Pet updatedPet = new Pet(10, "NonExistent", 5, "NoOwner");
        Pet result = petDao.updatePet(10, updatedPet);

        assertNull(result);
    }

    /**
     * Test to verify that a pet is deleted successfully.
     */
    @Test
    void deletePet_ShouldDeletePet() {
        boolean result = petDao.deletePet(1);

        assertTrue(result);
        assertEquals(5, petDao.getPets().size());
    }

    /**
     * Test to verify that deleting a non-existing pet returns false.
     */
    @Test
    void deletePet_ShouldReturnFalse_WhenPetNotFound() {
        boolean result = petDao.deletePet(10);

        assertFalse(result);
    }
}
