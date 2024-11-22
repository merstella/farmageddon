package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class Player extends Entity{
    static public final int WIDTH = 32;
    static public final int HEIGHT = 32;
    static public final float scale = 4;
    private PlayerAnimation animation;
    private PlayerAnimation.Direction currentDirection;

    // danh sach vat pham su dung trong kho do
    public ArrayList<Items> inventory;
    public ArrayList<Items> eqipInventory;
    private final int maxInventorySize = 25;
    private final int maxEqipInventorySize = 5;
    public int money = 0;


    public Player(float x, float y, float speed) {
        super(x, y, speed);
        animation = new PlayerAnimation();  // Initialize animation instance
        currentDirection = PlayerAnimation.Direction.IDLE_DOWN;  // Default direction
        this.inventory = new ArrayList<>();
        this.eqipInventory = new ArrayList<>();
    }


    // inventory contact
    public void setEquipItem(Items item) {
        eqipInventory.add(item);
    }

    public void removeEquipItem(Items item) {
        eqipInventory.remove(item);
    }

    public void setItem(Items item) {
        inventory.add(item);
    }

    public void removeItem(Items item) {
        inventory.remove(item);
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void subMoney(int amount) {
        money -= amount;
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        Vector2 movement = new Vector2(0, 0);

        // Check input and set movement direction
        boolean up = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        if (up) movement.y += 1;
        if (down) movement.y -= 1;
        if (right) movement.x += 1;
        if (left) movement.x -= 1;

        // Normalize movement to ensure consistent speed in all directions
        if (!movement.isZero()) {
            movement.nor().scl(speed * delta); // Scale by speed and delta time
            x += movement.x;
            y += movement.y;

            // Set direction for animation
            if (up && right) {
                currentDirection = PlayerAnimation.Direction.UP_RIGHT;
            } else if (up && left) {
                currentDirection = PlayerAnimation.Direction.UP_LEFT;
            } else if (down && right) {
                currentDirection = PlayerAnimation.Direction.DOWN_RIGHT;
            } else if (down && left) {
                currentDirection = PlayerAnimation.Direction.DOWN_LEFT;
            } else if (up) {
                currentDirection = PlayerAnimation.Direction.UP;
            } else if (down) {
                currentDirection = PlayerAnimation.Direction.DOWN;
            } else if (right) {
                currentDirection = PlayerAnimation.Direction.RIGHT;
            } else if (left) {
                currentDirection = PlayerAnimation.Direction.LEFT;
            }
        } else {
            if (currentDirection == PlayerAnimation.Direction.UP) {
                currentDirection = PlayerAnimation.Direction.IDLE_UP;
            } else if (currentDirection == PlayerAnimation.Direction.DOWN) {
                currentDirection = PlayerAnimation.Direction.IDLE_DOWN;
            } else if (currentDirection == PlayerAnimation.Direction.LEFT) {
                currentDirection = PlayerAnimation.Direction.IDLE_LEFT;
            } else if (currentDirection == PlayerAnimation.Direction.RIGHT) {
                currentDirection = PlayerAnimation.Direction.IDLE_RIGHT;
            } else if (currentDirection == PlayerAnimation.Direction.DOWN_LEFT) {
                currentDirection = PlayerAnimation.Direction.IDLE_DOWN_LEFT;
            } else if (currentDirection == PlayerAnimation.Direction.DOWN_RIGHT) {
                currentDirection = PlayerAnimation.Direction.IDLE_DOWN_RIGHT;
            } else if (currentDirection == PlayerAnimation.Direction.UP_LEFT) {
                currentDirection = PlayerAnimation.Direction.IDLE_UP_LEFT;
            } else if (currentDirection == PlayerAnimation.Direction.UP_RIGHT) {
                currentDirection = PlayerAnimation.Direction.IDLE_UP_RIGHT;
            }

        }
    }

    @Override
    public void render(SpriteBatch batch) {
        animation.render(batch, x, y, currentDirection);
    }

    public void dispose() {
        animation.dispose();  // Dispose of resources when done
    }
}
