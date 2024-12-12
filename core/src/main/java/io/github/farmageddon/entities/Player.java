package io.github.farmageddon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.Main;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.CollisionHandling;
import io.github.farmageddon.ultilites.FishingMinigame;
import io.github.farmageddon.ultilites.Items;

import java.util.ArrayList;
import java.util.Objects;

public class Player extends Entity {

    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;
    private Main game;
    private GameScreen gameScreen;
    public static Animator animation;
    public Animator.Direction currentDirection;
    public Animator.Activity currentActivity;
    public ShapeRenderer shapeRenderer;
    // danh sach vat pham su dung trong kho do
    public ArrayList<Items> inventory;
    public ArrayList<Items> eqipInventory;
    public final int maxInventorySize = 25;
    public final int maxEqipInventorySize = 5;
    public int slotCursor = 0;
    public int inventoryCursor = 0;
    public int money = 100;
    private CollisionHandling collisionHandling;
    public Rectangle playerBounds;
    public static boolean hasStartedFishing;
    // Attack properties
    public static int attackDamage = 10;  // Attack damage
    public static float attackRange = 50f;  // Attack range
    public static float attackCooldown = 0.5f;  // Time between attacks (cooldown)
    public static float timeSinceLastAttack = 0f;  // Timer to track time since last attack
    public static String itemHolding;

    private float attackStateTimer;
    @Override
    public void setBeingAttacked(boolean beingAttacked) {
        super.setBeingAttacked(beingAttacked);
        if (beingAttacked) {
            attackStateTimer = 0.2f;
        }
    }
    public Player(float x, float y, float speed) {
        super(x, y, speed, true, 100000);
        animation = new Animator(); // Initialize animation instance
        currentDirection = Animator.Direction.IDLE_DOWN; // Default direction
        currentActivity = Animator.Activity.NONE; // Default activity
        shapeRenderer = new ShapeRenderer();
        this.inventory = new ArrayList<>();
        this.eqipInventory = new ArrayList<>();
        itemHolding = "none";
        playerBounds = new Rectangle(x + 7, y + 9, 14, 9);
    }

    // inventory contact
    public void setEquipItem(Items item, int index) {
        eqipInventory.set(index, item);
    }

    public void removeEquipItem(Items item) {
        eqipInventory.remove(item);
    }

    public void setItem(Items item, int index) {
        inventory.set(index, item);
    }

    public void removeItem(Items item) {
        inventory.set(inventoryCursor,gameScreen.Default);
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void subMoney(int amount) {
        money -= amount;
    }
    @Override
    public void takeDamage(float damage) {
        super.takeDamage(damage);
        setBeingAttacked(true);
    }

    private Animator.Activity getHitAnimation(Animator.Direction currentDirection) {
        String direction = currentDirection.toString();
        if (direction.contains("UP")) {
            if (direction.contains("LEFT")) {
                return Animator.Activity.HIT_UP_LEFT;
            }
            if (direction.contains("RIGHT")){
                return Animator.Activity.HIT_UP_RIGHT;
            }
            return Animator.Activity.HIT_UP;
        } else if (direction.contains("DOWN")) {
            if (direction.contains("LEFT")) {
                return Animator.Activity.HIT_DOWN_LEFT;
            }
            if (direction.contains("RIGHT")){
                return Animator.Activity.HIT_DOWN_RIGHT;
            }
            return Animator.Activity.HIT_DOWN;
        } else if (direction.contains("LEFT")) {
            return Animator.Activity.HIT_LEFT;
        } else if (direction.contains("RIGHT")) {
            return Animator.Activity.HIT_RIGHT;
        }
        return currentActivity;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (beingAttacked) {
            attackStateTimer -= delta;
            if (attackStateTimer <= 0) {
                beingAttacked = false;
            }
        }
        timeSinceLastAttack += delta;
        updateDirectionAnimation(delta);

    }



    public void updateFishingAnimation() {
        currentActivity = getFishingDirection();
//        System.out.println(currentActivity);
    }

    public void updateActivityAnimation(String type, Vector2 lookPoint) {
        currentActivity = getFacingDirection(type, lookPoint);
    }
    public void stopActivity() {
        currentActivity = Animator.Activity.NONE;
    }

    private void updateDirectionAnimation(float delta) {
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
                switch (itemHolding){
                    case "none":
                        currentDirection = Animator.Direction.UP_RIGHT;
                        break;
                    default:
                        currentDirection = Animator.Direction.TORCH_UP_RIGHT;
                        break;
                }

            } else if (up && left) {
                switch (itemHolding){
                    case "none":
                        currentDirection = Animator.Direction.UP_LEFT;
                        break;
                    default:
                        currentDirection = Animator.Direction.TORCH_UP_LEFT;
                        break;
                }
            } else if (down && right) {
                switch (itemHolding){
                    case "none":
                        currentDirection = Animator.Direction.DOWN_RIGHT;
                        break;
                    default:
                        currentDirection = Animator.Direction.TORCH_DOWN_RIGHT;
                        break;
                }
            } else if (down && left) {
                switch (itemHolding){
                    case "none":
                        currentDirection = Animator.Direction.DOWN_LEFT;
                        break;
                    default:
                        currentDirection = Animator.Direction.TORCH_DOWN_LEFT;
                        break;
                }
            } else if (up) {
                switch (itemHolding){
                    case "none":
                        currentDirection = Animator.Direction.UP;
                        break;
                    default:
                        currentDirection = Animator.Direction.TORCH_UP;
                        break;
                }
            } else if (down) {
                switch (itemHolding){
                    case "none":
                        currentDirection = Animator.Direction.DOWN;
                        break;
                    default:
                        currentDirection = Animator.Direction.TORCH_DOWN;
                        break;
                }
            } else if (right) {
                switch (itemHolding){
                    case "none":
                        currentDirection = Animator.Direction.RIGHT;
                        break;
                    default:
                        currentDirection = Animator.Direction.TORCH_RIGHT;
                        break;
                }
            } else if (left) {
                switch (itemHolding){
                    case "none":
                        currentDirection = Animator.Direction.LEFT;
                        break;
                    default:
                        currentDirection = Animator.Direction.TORCH_LEFT;
                        break;
                }
            }
        } else {
            // Idle animations based on last movement direction
            currentDirection = getIdleDirection(currentDirection);
        }
    }


    private Animator.Direction getIdleDirection(Animator.Direction direction) {
        switch (direction) {
            case UP:
                return Animator.Direction.IDLE_UP;
            case DOWN:
                return Animator.Direction.IDLE_DOWN;
            case LEFT:
                return Animator.Direction.IDLE_LEFT;
            case RIGHT:
                return Animator.Direction.IDLE_RIGHT;
            case DOWN_LEFT:
                return Animator.Direction.IDLE_DOWN_LEFT;
            case DOWN_RIGHT:
                return Animator.Direction.IDLE_DOWN_RIGHT;
            case UP_LEFT:
                return Animator.Direction.IDLE_UP_LEFT;
            case UP_RIGHT:
                return Animator.Direction.IDLE_UP_RIGHT;
            case TORCH_UP:
                return Animator.Direction.IDLE_TORCH_UP;
            case TORCH_DOWN:
                return Animator.Direction.IDLE_TORCH_DOWN;
            case TORCH_LEFT:
                return Animator.Direction.IDLE_TORCH_LEFT;
            case TORCH_RIGHT:
                return Animator.Direction.IDLE_TORCH_RIGHT;
            case TORCH_DOWN_LEFT:
                return Animator.Direction.IDLE_TORCH_DOWN_LEFT;
            case TORCH_DOWN_RIGHT:
                return Animator.Direction.IDLE_TORCH_DOWN_RIGHT;
            case TORCH_UP_LEFT:
                return Animator.Direction.IDLE_TORCH_UP_LEFT;
            case TORCH_UP_RIGHT:
                return Animator.Direction.IDLE_TORCH_UP_RIGHT;
            default:
                return direction;
        }
    }

    public Animator.Activity getFacingDirection(String type, Vector2 lookPoint) {
        // Calculate the difference vector
        Vector2 diff = new Vector2(lookPoint).sub(new Vector2(position.x + 16, position.y + 16));

        // Normalize the vector to handle directional calculations
        float angle = diff.angleDeg();

        // Determine direction based on angle (adjusted to a 360-degree circle)
        if (angle >= 45 && angle < 135) {
            if (Objects.equals(type, "hoe")) {
                return Animator.Activity.HOE_UP;
            } else if (Objects.equals(type, "bucket")){
                return Animator.Activity.WATER_UP;
            } else if (Objects.equals(type, "sword")){
                return Animator.Activity.ATTACK_UP;
            }
        } else if (angle >= 135 && angle < 225) {
            if (Objects.equals(type, "hoe")) {
                return Animator.Activity.HOE_LEFT;
            } else if (Objects.equals(type, "bucket")){
                return Animator.Activity.WATER_LEFT;
            } else if (Objects.equals(type, "sword")){
                return Animator.Activity.ATTACK_LEFT;
            }
        } else if (angle >= 225 && angle < 315) {
            if (Objects.equals(type, "hoe")) {
                return Animator.Activity.HOE_DOWN;
            } else if (Objects.equals(type, "bucket")){
                return Animator.Activity.WATER_DOWN;
            } else if (Objects.equals(type, "sword")){
                return Animator.Activity.ATTACK_DOWN;
            }
        } else {
            if (Objects.equals(type, "hoe")) {
                return Animator.Activity.HOE_RIGHT;
            } else if (Objects.equals(type, "bucket")){
                return Animator.Activity.WATER_RIGHT;
            } else if (Objects.equals(type, "sword")){
                return Animator.Activity.ATTACK_RIGHT;
            }
        }
        return Animator.Activity.NONE;
    }

    public Animator.Activity getFishingDirection() {
        if (!hasStartedFishing) {
            hasStartedFishing = true; // Đánh dấu rằng hành động đã bắt đầu
            if (GameScreen.cursorRight) {
                return Animator.Activity.START_FISHING_RIGHT;
            }
            else if (GameScreen.cursorLeft) {
                return Animator.Activity.START_FISHING_LEFT;
            }
        }
        return Animator.Activity.NONE;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        batch.begin();
        if (isBeingAttacked()) {
//            System.out.println(currentDirection);
            animation.renderActivity(batch, position.x, position.y, getHitAnimation(currentDirection), GameScreen.stateTime);
        } else if (currentActivity == Animator.Activity.NONE) {
            animation.render(batch, position.x, position.y, currentDirection, GameScreen.stateTime);
        } else {
            animation.renderActivity(batch, position.x, position.y, currentActivity, GameScreen.stateTime);
//            System.out.println(currentActivity);
        }
        batch.end();
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

    public float getAttackDamage() {
        return attackDamage;
    }
}
