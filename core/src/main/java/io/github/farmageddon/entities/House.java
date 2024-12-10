package io.github.farmageddon.entities;

import java.util.ArrayList;
import java.util.List;

public class House {
    private List<HouseEntity> entitiesOnBorder;  // List of entities on the border
    private float totalHealth;  // Total health of the house (sum of health of entities)
    private float currentHealth;  // Current health of the house

    public House(float totalHealth) {
        this.entitiesOnBorder = new ArrayList<>();
        this.totalHealth = totalHealth;
        this.currentHealth = totalHealth;
    }

    // Add a HouseEntity (entity on the border) to the house
    public void addEntity(HouseEntity entity) {
        entitiesOnBorder.add(entity);
    }

    // Calculate the total health (sum of all entities' health)
    public void calculateHealth() {
        currentHealth = 0;
        for (HouseEntity entity : entitiesOnBorder) {
            currentHealth += entity.getHealth();
        }
    }

    // Deal damage to the house (through an entity)
    public void takeDamage(float damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            destroyHouse();
        }
    }

    // Destroy the house when health reaches 0
    private void destroyHouse() {
        // Handle house destruction logic (e.g., remove house, notify other systems)
        System.out.println("House destroyed!");
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public List<HouseEntity> getEntitiesOnBorder() {
        return entitiesOnBorder;
    }
}
