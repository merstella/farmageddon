package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.ultilites.Items;
import java.util.ArrayList;
import java.util.Objects;

public class Player extends Entity {

    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;

    private PlayerAnimation animation;
    public PlayerAnimation.Direction currentDirection;
    public PlayerAnimation.Activity currentActivity;
    public ShapeRenderer shapeRenderer;
    // danh sach vat pham su dung trong kho do
    public static ArrayList<Items.Item> inventory;
    public static ArrayList<Items.Item> eqipInventory;
    private final int maxInventorySize = 25;
    private final int maxEqipInventorySize = 5;
    public static int slotCursor;
    public int money = 0;
    private CollisionHandling collisionHandling;
    Rectangle playerBounds;
    private boolean hasStartedFishing;

    public Player(float x, float y, float speed) {
        super(x, y, speed, false, 100);
        animation = new PlayerAnimation(); // Initialize animation instance
        currentDirection = PlayerAnimation.Direction.IDLE_DOWN; // Default direction
        currentActivity = PlayerAnimation.Activity.NONE; // Default activity
        shapeRenderer = new ShapeRenderer();
        this.inventory = new ArrayList<>();
        this.eqipInventory = new ArrayList<>();
        playerBounds = new Rectangle(x + 7, y + 9, 14, 9);
    }
    // inventory contact
    public void setEquipItem(Items.Item item) {
        eqipInventory.add(item);
    }

    public void removeEquipItem(Items.Item item) {
        eqipInventory.remove(item);
    }

    public void setItem(Items.Item item) {
        inventory.add(item);
    }

    public void removeItem(Items.Item item) {
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

    public void updateFishingAnimation() {
        currentActivity = getFishingDirection();
    }

    public void updateActivityAnimation(String type, Vector2 lookPoint) {
        currentActivity = getFacingDirection(type, lookPoint);
    }
    public void stopActivity() {
        currentActivity = PlayerAnimation.Activity.NONE;
    }

    private void updateDirectionAnimation(float delta) {
        float tmpSpeed = speed;
        Vector2 movement = new Vector2(0, 0);

        boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);

        if (up) movement.y += 1;
        if (down) movement.y -= 1;
        if (right) movement.x += 1;
        if (left) movement.x -= 1;

        // Normalize movement to ensure consistent speed in all directions
        if (!movement.isZero()) {
            movement.nor().scl(speed * delta); // Scale by speed and delta time

            // Create test bounds to simulate the next position
            Rectangle testBounds = new Rectangle(playerBounds);
            testBounds.setPosition(playerBounds.x + movement.x, playerBounds.y + movement.y);

            // Check for collision only at the new position
            if (!CollisionHandling.isColliding(testBounds)) {
                position.add(movement); // Update position if no collision
                playerBounds.setPosition(position.x + 7, position.y + 9);// Update bounds position
            }

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

    public PlayerAnimation.Activity getFacingDirection(String type, Vector2 lookPoint) {
        // Calculate the difference vector
        Vector2 diff = new Vector2(lookPoint).sub(new Vector2(position.x + 16, position.y + 16));

        // Normalize the vector to handle directional calculations
        float angle = diff.angleDeg();

        // Determine direction based on angle (adjusted to a 360-degree circle)
        if (angle >= 45 && angle < 135) {
            if (Objects.equals(type, "hoe")) {
                return PlayerAnimation.Activity.HOE_UP;
            } else {
                return PlayerAnimation.Activity.WATER_UP;
            }
        } else if (angle >= 135 && angle < 225) {
            if (Objects.equals(type, "hoe")) {
                return PlayerAnimation.Activity.HOE_LEFT;
            } else {
                return PlayerAnimation.Activity.WATER_LEFT;
            } // Facing left
        } else if (angle >= 225 && angle < 315) {
            if (Objects.equals(type, "hoe")) {
                return PlayerAnimation.Activity.HOE_DOWN;
            } else {
                return PlayerAnimation.Activity.WATER_DOWN;
            } // Facing downward
        } else {
            if (Objects.equals(type, "hoe")) {
                return PlayerAnimation.Activity.HOE_RIGHT;
            } else {
                return PlayerAnimation.Activity.WATER_RIGHT;
            } // Facing right
        }
    }

    public PlayerAnimation.Activity getFishingDirection() {
//        if ((getPosition().x < 896f && getPosition().x > 891f && getPosition().y < 485f && getPosition().y > 480f)  ) {
//            return PlayerAnimation.Activity.START_FISHING_RIGHT;
//        }
//        else return null;
        if (!hasStartedFishing) {
            hasStartedFishing = true; // Đánh dấu rằng hành động đã bắt đầu
            return PlayerAnimation.Activity.WAIT_FISHING_RIGHT;
        }
        return PlayerAnimation.Activity.NONE;
    }


    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if (currentActivity == PlayerAnimation.Activity.NONE) {
            animation.render(batch, position.x, position.y, currentDirection);
        } else {
            animation.renderActivity(batch, position.x, position.y, currentActivity);
        }
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Draw playerBounds in red
        shapeRenderer.setColor(1, 0, 0, 1); // Red color
        shapeRenderer.rect(playerBounds.x, playerBounds.y, playerBounds.width, playerBounds.height);
        shapeRenderer.end();
    }

    public void dispose() {
        animation.dispose(); shapeRenderer.dispose();// Dispose of resources when done
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return playerBounds;
    }
}
