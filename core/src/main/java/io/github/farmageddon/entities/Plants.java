//
//package io.github.farmageddon.entities;
//
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.math.Rectangle;
//import io.github.farmageddon.PlantsAnimation;
//
//import java.util.ArrayList;
//
//public class Plants extends Entity {
//    private final Animator animation;
//    public Animator.plant currentDirection;
//    public Plants(float x, float y, int maxHealth) {
//        super(x, y, 0, false, maxHealth);
//        animation = new Animator(); // Initialize animation instance
//        currentActivity = Animator.Activity.NONE; // Default activity
//        shapeRenderer = new ShapeRenderer();
//        this.inventory = new ArrayList<>();
//        this.eqipInventory = new ArrayList<>();
//        playerBounds = new Rectangle(x + 7, y + 9, 14, 9);
//    }
//    // Getter và Setter cho các thuộc tính
//    public float getX() {
//        return x;
//    }
//
//    public float getY() {
//        return y;
//    }
//
//    public int getHealth() {
//        return health;
//    }
//    public void render(SpriteBatch batch) {
//        batch.begin();
//        plantsAnimation.render(x, y,PlantsAnimation.PlantsState.IDLE);
//        batch.end();
//    }
//    public void dispose() {
//        plantsAnimation.dispose();
//        batch.dispose();
//    }
//
//}
