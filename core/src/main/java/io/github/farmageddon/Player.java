package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;

    private PlayerAnimation animation;
    private PlayerAnimation.Direction currentDirection;
    private PlayerAnimation.Activity currentActivity;
    private boolean performingActivity; // Flag to track if player is performing an activity

    public Player(float x, float y, float speed) {
        super(x, y, speed);
        animation = new PlayerAnimation(); // Initialize animation instance
        currentDirection = PlayerAnimation.Direction.IDLE_DOWN; // Default direction
        currentActivity = PlayerAnimation.Activity.NONE; // Default activity
        performingActivity = false; // Initially not performing any activity
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (performingActivity) {
            // If performing an activity, reduce its duration or handle its end
            handleActivity(delta);
            return; // Skip movement updates while performing an activity
        }

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

    private void handleActivity(float delta) {
        // Reduce activity timer (if applicable) or end the activity
        if (animation.isActivityFinished(currentActivity)) {
            performingActivity = false;
            currentActivity = PlayerAnimation.Activity.NONE; // Reset to no activity
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

    public void startActivity(Vector2 cropPosition) {
        // Determine the activity direction based on the crop's position
        System.out.println("CAC "+position.x+"  " +position.y+"  "+ cropPosition.x+" "+ cropPosition.y);

        currentActivity = getFacingDirection(cropPosition);
        System.out.print(currentActivity);
        performingActivity = true;
    }
    public void stopActivity() {
        performingActivity = false;
        currentActivity = PlayerAnimation.Activity.NONE;
    }

    public PlayerAnimation.Activity getFacingDirection(Vector2 lookPoint) {
        // Calculate the difference vector
        Vector2 diff = new Vector2(lookPoint).sub(position);

        // Compare absolute values to determine which quarter
        if (Math.abs(diff.y) > Math.abs(diff.x)) {
            return diff.y < 0 ? PlayerAnimation.Activity.HOE_DOWN : PlayerAnimation.Activity.HOE_UP;
        } else {
            return diff.x > 0 ? PlayerAnimation.Activity.HOE_RIGHT : PlayerAnimation.Activity.HOE_LEFT;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (performingActivity) {
            animation.renderActivity(batch, position.x, position.y, currentActivity); // Render activity animation
        } else {
            animation.render(batch, position.x, position.y, currentDirection); // Render movement or idle animation
        }
    }

    public void dispose() {
        animation.dispose(); // Dispose of resources when done
    }

    public void setIsDoingActivity(boolean performingActivity) {
        this.performingActivity = performingActivity;
    }
    public boolean isPerformingActivity() {
        return performingActivity;
    }
}
