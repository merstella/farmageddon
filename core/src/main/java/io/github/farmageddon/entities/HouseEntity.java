package io.github.farmageddon.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HouseEntity extends Entity {
    private House house;  // The house this entity belongs to

    public HouseEntity(float x, float y, float speed, House house) {
        super(x, y, speed, true, 1000000000);  // Houses don't have their own health, they have a health aggregate
        this.house = house;
    }

    public House getHouse() {
        return house;
    }

    // The entity does not move or attack but interacts with the house's health
    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
    }

    @Override
    public void takeDamage(float damage) {
        damageHouse(damage);
    }
    // In case you need to interact with the house, for example, damage it
    public void damageHouse(float damage) {
        house.takeDamage(damage);
    }
}
