package fr.elsior.wfx.test.services;

import fr.elsior.wfx.test.exceptions.OwnerNotFoundException;
import fr.elsior.wfx.test.exceptions.PetAlreadyExistsException;
import fr.elsior.wfx.test.exceptions.PetNotFoundException;
import fr.elsior.wfx.test.model.Pet;
import fr.elsior.wfx.test.services.dao.IPetDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @Author: Elimane
 */
class PetServiceImplTest {

    @Mock
    private IPetDao petDao;

    @InjectMocks
    private PetServiceImpl petService;

    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pet1 = new Pet(1, "Rex", 5, "Jack");
        pet2 = new Pet(2, "Bella", 3, "Alice");
    }

    /**
     * Test to verify that the getPets() method returns all pets.
     */
    @Test
    void getPets_ShouldReturnAllPets() {
        List<Pet> pets = Arrays.asList(pet1, pet2);
        when(petDao.getPets()).thenReturn(pets);

        StepVerifier.create(petService.getPets())
                .expectNext(pet1, pet2)
                .verifyComplete();

        verify(petDao, times(1)).getPets();
    }

    /**
     * Test to verify that the addPet() method adds a new pet.
     */
    @Test
    void addPet_ShouldAddNewPet() throws PetAlreadyExistsException {
        when(petDao.addPet(pet1)).thenReturn(pet1);

        StepVerifier.create(petService.addPet(pet1))
                .expectNext(pet1)
                .verifyComplete();

        verify(petDao, times(1)).addPet(pet1);
    }

    /**
     * Test to verify that the addPet() method throws an exception if the pet already exists.
     */
    @Test
    void addPet_ShouldThrowException_WhenPetAlreadyExists() throws PetAlreadyExistsException {
        when(petDao.addPet(pet1)).thenThrow(new PetAlreadyExistsException());

        StepVerifier.create(petService.addPet(pet1))
                .expectError(PetAlreadyExistsException.class)
                .verify();

        verify(petDao, times(1)).addPet(pet1);
    }

    /**
     * Test to verify that the getPet() method returns a pet by ID.
     */
    @Test
    void getPet_ShouldReturnPetById() throws PetNotFoundException {
        when(petDao.getPet(1)).thenReturn(pet1);

        StepVerifier.create(petService.getPet(1))
                .expectNext(pet1)
                .verifyComplete();

        verify(petDao, times(1)).getPet(1);
    }

    /**
     * Test to verify that the getPet() method throws an exception if the pet is not found.
     */
    @Test
    void getPet_ShouldThrowException_WhenPetNotFound() throws PetNotFoundException {
        when(petDao.getPet(1)).thenThrow(new PetNotFoundException());

        StepVerifier.create(petService.getPet(1))
                .expectError(PetNotFoundException.class)
                .verify();

        verify(petDao, times(1)).getPet(1);
    }

    /**
     * Test to verify that the getPetsByOwner() method returns pets for a given owner.
     */
    @Test
    void getPetsByOwner_ShouldReturnPetsByOwner() throws OwnerNotFoundException {
        List<Pet> pets = Arrays.asList(pet1);
        when(petDao.getPetsByOwner("Jack")).thenReturn(pets);

        StepVerifier.create(petService.getPetsByOwner("Jack"))
                .expectNext(pet1)
                .verifyComplete();

        verify(petDao, times(1)).getPetsByOwner("Jack");
    }

    /**
     * Test to verify that the getPetsByOwner() method throws an exception if the owner is not found.
     */
    @Test
    void getPetsByOwner_ShouldThrowException_WhenOwnerNotFound() throws OwnerNotFoundException {
        when(petDao.getPetsByOwner("Unknown")).thenThrow(new OwnerNotFoundException());

        StepVerifier.create(petService.getPetsByOwner("Unknown"))
                .expectError(OwnerNotFoundException.class)
                .verify();

        verify(petDao, times(1)).getPetsByOwner("Unknown");
    }

    /**
     * Test to verify that the updatePet() method updates a pet.
     */
    @Test
    void updatePet_ShouldUpdatePet() throws PetNotFoundException {
        Pet updatedPet = new Pet(1, "Rex", 6, "Jack");
        when(petDao.updatePet(1, updatedPet)).thenReturn(updatedPet);

        StepVerifier.create(petService.updatePet(1, updatedPet))
                .expectNext(updatedPet)
                .verifyComplete();

        verify(petDao, times(1)).updatePet(1, updatedPet);
    }

    /**
     * Test to verify that the updatePet() method throws an exception if the pet is not found.
     */
    @Test
    void updatePet_ShouldThrowException_WhenPetNotFound() throws PetNotFoundException {
        Pet updatedPet = new Pet(1, "Rex", 6, "Jack");
        when(petDao.updatePet(1, updatedPet)).thenThrow(new PetNotFoundException());

        StepVerifier.create(petService.updatePet(1, updatedPet))
                .expectError(PetNotFoundException.class)
                .verify();

        verify(petDao, times(1)).updatePet(1, updatedPet);
    }

    /**
     * Test to verify that the deletePet() method deletes a pet.
     */
    @Test
    void deletePet_ShouldDeletePet() throws PetNotFoundException {
        when(petDao.deletePet(1)).thenReturn(true);

        assertDoesNotThrow(() -> petService.deletePet(1));
        verify(petDao, times(1)).deletePet(1);
    }


    /**
     * Test to verify that the deletePet() method throws an exception if the pet is not found.
     */
    @Test
    void deletePet_ShouldThrowException_WhenPetNotFound() throws PetNotFoundException {
        doThrow(new PetNotFoundException()).when(petDao).deletePet(8);

        assertThrows(PetNotFoundException.class, () -> petService.deletePet(8));
        verify(petDao, times(1)).deletePet(8);
    }
}
