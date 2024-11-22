package io.github.farmageddon;

public class HealthBar {
    private float maxHealth;
    private float currentHealth;
    private float width;
    private float height;

    public HealthBar(float maxHealth, int width, int height) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.width = width;
        this.height = height;
    }

    public void setHealth(float health) {
        currentHealth = health;
    }


}
