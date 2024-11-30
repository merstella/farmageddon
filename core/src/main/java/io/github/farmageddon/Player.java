package io.github.farmageddon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.markets.Items;
import io.github.farmageddon.screens.GameScreen;

import java.util.ArrayList;

public class Player extends Entity {

    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;

    public static PlayerAnimation animation;
    public PlayerAnimation.Direction currentDirection;
    public static PlayerAnimation.Activity currentActivity;
    public ShapeRenderer shapeRenderer;
    // danh sach vat pham su dung trong kho do
    public static ArrayList<Items> inventory;
    public static ArrayList<Items> eqipInventory;
    private final int maxInventorySize = 25;
    private final int maxEqipInventorySize = 5;
    public static int slotCursor;
    public int money = 0;
    public Vector2 position;
    private CollisionHandling collisionHandling;
    Rectangle playerBounds;

    // Fishing
    public static boolean hasStartedFishing;
    private boolean isFishing;
    private boolean fishingComplete;
    public FishingMinigame minigame;
    private float elapsedTime = 0f;

    public Player(float x, float y, float speed) {
        super(x, y, speed);
        position = new Vector2(x, y);
        animation = new PlayerAnimation(); // Initialize animation instance
        currentDirection = PlayerAnimation.Direction.IDLE_DOWN; // Default direction
        currentActivity = PlayerAnimation.Activity.NONE; // Default activity
        shapeRenderer = new ShapeRenderer();
        this.inventory = new ArrayList<>();
        this.eqipInventory = new ArrayList<>();
        playerBounds = new Rectangle(x + 7, y + 9, 14, 9);
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
        minigame = new FishingMinigame();
        minigame.create();
    }

    public void updateFishingAnimation() {
        currentActivity = getFishingDirection();
        //handleFishingLogic();
    }

    public void updateActivityAnimation(Vector2 lookPoint) {
        currentActivity = getFacingDirection(lookPoint);
    }
    public void stopActivity() {
        currentActivity = PlayerAnimation.Activity.NONE;
    }

    private void updateDirectionAnimation(float delta) {
        float tmpSpeed = speed;
        Vector2 movement = new Vector2(0, 0);
//        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && !isFishing) {
//            startFishing();
//        }
//
//        if (isFishing) {
//            handleFishingLogic(delta);
//        } else {
//            updateDirectionAnimation(delta);
//        }

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

////    private void startFishing() {
////        isFishing = true;
////        currentActivity = PlayerAnimation.Activity.START_FISHING_RIGHT;
////    }
////
////    private void finishFishing() {
////        isFishing = false;
////        fishingComplete = false;
////        currentActivity = PlayerAnimation.Activity.NONE;
////
////        // Add a Fish item to the inventory
////        //Items fish = new Items("Fish");
////        if (inventory.size() < maxInventorySize) {
//////            inventory.add(fish);
////            System.out.println("You caught a fish! Added to inventory.");
////        } else {
////            System.out.println("Inventory full. Cannot add fish.");
////        }
////    }
//
//    private void handleFishingLogic() {
//        switch (currentActivity) {
//            case START_FISHING_RIGHT:
//                System.out.println("bắt đầu fishing");
//                if (animation.actionAnimations[currentActivity.ordinal()].isAnimationFinished(animation.stateTime)) {
////                elapsedTime += Gdx.graphics.getDeltaTime();
////                elapsedTime += delta;
////                if (elapsedTime >= 2.0f){
////                   minigame.render();
////                    if (minigame.gameOver == true)
//                        currentActivity = PlayerAnimation.Activity.WAIT_FISHING_RIGHT;
//                        if (minigame.gameOver == false){
//                            System.out.println("Câu cá thôi!");
//                            minigame.render();
//                        }
//                        else {
////                        System.out.println("Câu cá thôi!");
//                            animation.stateTime = 0; // Reset animation time
//                            elapsedTime = 0f;
//                            fishingComplete = true;
//                        }
//                }
//                break;
//
//            case WAIT_FISHING_RIGHT:
//                if (fishingComplete) {
//                    currentActivity = PlayerAnimation.Activity.DONE_FISHING_RIGHT;
//                    animation.stateTime = 0; // Reset animation time
//                }
//                break;
//
//            case DONE_FISHING_RIGHT:
//                if (animation.actionAnimations[currentActivity.ordinal()].isAnimationFinished(animation.stateTime)) {
//                    stopActivity();
//                }
//                break;
//
//            default:
//                break;
//        }
//    }

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
        Vector2 diff = new Vector2(lookPoint).sub(new Vector2(position.x + 16, position.y + 16));

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


    public PlayerAnimation.Activity getFishingDirection() {
        if (!hasStartedFishing) {
            hasStartedFishing = true; // Đánh dấu rằng hành động đã bắt đầu
            if (GameScreen.cursorRight == true) {
                return PlayerAnimation.Activity.START_FISHING_RIGHT;
            }
            else if (GameScreen.cursorLeft == true) {
                return PlayerAnimation.Activity.START_FISHING_LEFT;
            }
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
}
