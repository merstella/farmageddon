package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.markets.Items;

import java.util.ArrayList;

public class Player extends Entity {
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;

    private PlayerAnimation animation;
    public PlayerAnimation.Direction currentDirection;
    public PlayerAnimation.Activity currentActivity;
    public ShapeRenderer shapeRenderer;
    // danh sach vat pham su dung trong kho do
    public ArrayList<Items> inventory;
    public ArrayList<Items> eqipInventory;
    private final int maxInventorySize = 25;
    private final int maxEqipInventorySize = 5;
    public int money = 0;

    public Player(float x, float y, float speed) {
        super(x, y, speed);
        animation = new PlayerAnimation(); // Initialize animation instance
        currentDirection = PlayerAnimation.Direction.IDLE_DOWN; // Default direction
        currentActivity = PlayerAnimation.Activity.NONE; // Default activity
        shapeRenderer = new ShapeRenderer();
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
        updateDirectionAnimation(delta);
    }

    public void updateActivityAnimation(Vector2 lookPoint) {
        currentActivity = getFacingDirection(lookPoint);
    }
    public void stopActivity() {
        currentActivity = PlayerAnimation.Activity.NONE;
    }

    private void updateDirectionAnimation(float delta) {
        Vector2 movement = new Vector2(0, 0);

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
            position.x += movement.x;
            position.y += movement.y;

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
            // Idle animations based on last movement direction
            currentDirection = getIdleDirection(currentDirection);
        }
    }

    private PlayerAnimation.Direction getIdleDirection(PlayerAnimation.Direction direction) {
        switch (direction) {
            case UP:
                return PlayerAnimation.Direction.IDLE_UP;
            case DOWN:
                return PlayerAnimation.Direction.IDLE_DOWN;
            case LEFT:
                return PlayerAnimation.Direction.IDLE_LEFT;
            case RIGHT:
                return PlayerAnimation.Direction.IDLE_RIGHT;
            case DOWN_LEFT:
                return PlayerAnimation.Direction.IDLE_DOWN_LEFT;
            case DOWN_RIGHT:
                return PlayerAnimation.Direction.IDLE_DOWN_RIGHT;
            case UP_LEFT:
                return PlayerAnimation.Direction.IDLE_UP_LEFT;
            case UP_RIGHT:
                return PlayerAnimation.Direction.IDLE_UP_RIGHT;
            default:
                return direction;
        }
    }

    public PlayerAnimation.Activity getFacingDirection(Vector2 lookPoint) {
        // Calculate the difference vector
        Vector2 diff = new Vector2(lookPoint).sub(position);

        // Normalize the vector to handle directional calculations
        float angle = diff.angleDeg();

        // Determine direction based on angle (adjusted to a 360-degree circle)
        if (angle >= 45 && angle < 135) {
            return PlayerAnimation.Activity.HOE_UP; // Facing upward
        } else if (angle >= 135 && angle < 225) {
            return PlayerAnimation.Activity.HOE_LEFT; // Facing left
        } else if (angle >= 225 && angle < 315) {
            return PlayerAnimation.Activity.HOE_DOWN; // Facing downward
        } else {
            return PlayerAnimation.Activity.HOE_RIGHT; // Facing right
        }
    }


    @Override
    public void render(SpriteBatch batch) {
        if (currentActivity == PlayerAnimation.Activity.NONE) {
            animation.render(batch, position.x, position.y, currentDirection);
        } else {
            animation.renderActivity(batch, position.x, position.y, currentActivity);
        }
    }


    public void dispose() {
        animation.dispose(); // Dispose of resources when done
    }

}
