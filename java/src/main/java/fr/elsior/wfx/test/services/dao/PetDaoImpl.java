package fr.elsior.wfx.test.services.dao;

import fr.elsior.wfx.test.model.Pet;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Elimane
 */
@Component
public class PetDaoImpl implements IPetDao {

    private final List<Pet> datas = new ArrayList<>();

    /**
     * Initializes the pet list with default values.
     * This method is called automatically after the bean is constructed.
     */
    @PostConstruct
    void initPetList() {
        datas.add(new Pet(1, "Medor", 12, "Peter"));
        datas.add(new Pet(2, "Mistigri", 5, "Jack"));
        datas.add(new Pet(3, "Pepette", 8, "Sarah"));
        datas.add(new Pet(4, "Snoopy", 21, "Sarah"));
        datas.add(new Pet(5, "Garfield", 8, "Sarah"));
        datas.add(new Pet(6, "Rex", 15, "Jack"));
    }

    /**
     * Retrieves the list of all pets.
     *
     * @return List of all pets.
     */
    @Override
    public List<Pet> getPets() {
        return datas;
    }

    /**
     * Adds a new pet to the list.
     *
     * @param pet The pet to add.
     * @return The added pet.
     */
    @Override
    public Pet addPet(Pet pet) {
        if (datas.stream().anyMatch(_pet -> _pet.getId() == pet.getId())) {
            return null;
        }
        datas.add(pet);
        return pet;
    }

    /**
     * Retrieves a pet by its ID.
     *
     * @param id The ID of the pet to retrieve.
     * @return The pet with the given ID or null if not found.
     */
    @Override
    public Pet getPet(int id) {
        return datas.stream()
                .filter(pet -> pet.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all pets owned by a specific owner.
     *
     * @param owner The owner's name.
     * @return List of pets owned by the specified owner or an empty list if none found.
     */
    @Override
    public List<Pet> getPetsByOwner(String owner) {
        return datas.stream()
                .filter(pet -> pet.getOwner().equalsIgnoreCase(owner))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing pet.
     *
     * @param id         The ID of the pet to update.
     * @param updatedPet The updated pet details.
     * @return The updated pet or null if not found.
     */
    @Override
    public Pet updatePet(int id, Pet updatedPet) {
        return datas.stream()
                .filter(pet -> pet.getId() == id)
                .findFirst()
                .map(pet -> {
                    pet.setName(updatedPet.getName());
                    pet.setAge(updatedPet.getAge());
                    pet.setOwner(updatedPet.getOwner());
                    return pet;
                })
                .orElse(null);
    }

    /**
     * Deletes a pet by its ID.
     *
     * @param id The ID of the pet to delete.
     * @return True if the pet was deleted, false otherwise.
     */
    @Override
    public boolean deletePet(int id) {
        return datas.removeIf(pet -> pet.getId() == id);
    }
}
