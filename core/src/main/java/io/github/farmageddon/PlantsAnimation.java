package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlantsAnimation {
    private static final int frameWidth = 32;
    private static final int frameHeight = 32;
    private final SpriteBatch batch; // Initialize SpriteBatch
    private final Texture plantsSheet;
    private final Animation<TextureRegion>[] plantsAnimations; // Array for animations by state
    private float stateTime;

    public enum PlantsState {
        IDLE,
        GROWING,
        WITHERING; // Additional states can be added as needed
    }

    public PlantsAnimation() {
        batch = new SpriteBatch(); // Initialize the SpriteBatch
        plantsSheet = new Texture(Gdx.files.internal("Crops.png"));
        plantsAnimations = new Animation[PlantsState.values().length]; // Initialize the animations array

        // Split the texture into regions
        TextureRegion[][] tmpFrames = TextureRegion.split(plantsSheet, frameWidth, frameHeight);

        // Create animations for each state
        plantsAnimations[PlantsState.IDLE.ordinal()] = createAnimation(tmpFrames[0], 0, 5);
        plantsAnimations[PlantsState.GROWING.ordinal()] = createAnimation(tmpFrames[1], 0, 5); // Example for GROWING
        plantsAnimations[PlantsState.WITHERING.ordinal()] = createAnimation(tmpFrames[2], 0, 5); // Example for WITHERING

        stateTime = 0f;
    }

    private Animation<TextureRegion> createAnimation(TextureRegion[] frame, int startFrame, int frameCount) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        System.arraycopy(frame, startFrame, directionFrames, 0, frameCount);
        return new Animation<>(0.1f, directionFrames); // Frame duration can be parameterized if needed
    }

    public void update(float deltaTime) {
        stateTime += deltaTime; // Update the elapsed time
    }

    public void render(float x, float y, PlantsState state) {
        // Get the current frame for the specified state
        TextureRegion currentFrame = plantsAnimations[state.ordinal()].getKeyFrame(stateTime, true);
        batch.begin();
        batch.draw(currentFrame, x, y, frameWidth, frameHeight); // Draw the current frame at (x, y)
        batch.end();
    }

    public void dispose() {
        batch.dispose(); // Dispose of the SpriteBatch
        plantsSheet.dispose(); // Dispose of the texture
    }
}
