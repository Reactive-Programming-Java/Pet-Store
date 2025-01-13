package fr.elsior.wfx.test.model;

/**
 * Author: Elimane
 */
public class Pet {
    int id;

    String name;

    int age;

    String owner;

    public Pet() {}




    public Pet(int id, String name, int age, String owner) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
