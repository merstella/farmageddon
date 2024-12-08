package io.github.farmageddon;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MonsterAnimation {

    private final Texture monsterSheet;
    private final Animation<TextureRegion>[] animations;
    private float stateTime;
    private static final int ATTACK_FRAME_WIDTH = 64; // Width of each frame for attacking animation
    private static final int ATTACK_FRAME_HEIGHT = 64; // Height of each frame for attacking animation

    public enum MonsterState {
        IDLE, WALKING, ATTACKING, DYING, BEINGATTACKED;
    }

    public MonsterAnimation() {
        // Load the monster sprite sheet
        monsterSheet = new Texture(Gdx.files.internal("Enemies\\Skeleton\\Skeleton_Swordman.png"));

        // Assuming the monster sprite sheet has 4 states with 4 frames each
        int frameWidth = 32; // Width of each frame
        int frameHeight = 32; // Height of each frame
        TextureRegion[][] tmpFrames = TextureRegion.split(monsterSheet, frameWidth, frameHeight);

        animations = new Animation[MonsterState.values().length];
        // Split the texture into frames for the attacking animation
        TextureRegion[][] attackTmpFrames = TextureRegion.split(monsterSheet, ATTACK_FRAME_WIDTH, ATTACK_FRAME_HEIGHT);

        // Create animations for each monster state
        animations[Direction.UP.ordinal()] = createAnimation(tmpFrames[0], 0, 5);
        animations[Direction.DOWN.ordinal()] = createAnimation(tmpFrames[1], 0, 5);
        animations[MonsterState.WALKING.ordinal()] = createAnimation(tmpFrames[1], 0, 5); // Walking state
        animations[MonsterState.DYING.ordinal()] = createAnimation(tmpFrames[5], 0, 2); // Dying state
        animations[MonsterState.IDLE.ordinal()] = createAnimation(tmpFrames[1], 0, 5);
        animations[MonsterState.ATTACKING.ordinal()] = createAnimation(attackTmpFrames[3], 0, 5);
        stateTime = 0f;
    }

    private Animation<TextureRegion> createAnimation(TextureRegion[] frames, int startFrame, int frameCount) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        System.arraycopy(frames, startFrame, directionFrames, 0, frameCount);
        return new Animation<>(0.2f, directionFrames);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime; // Update the elapsed time
    }
    public enum Direction {
        UP, DOWN;
    }

    // Render the attacking animation
    public void render(SpriteBatch batch, float x, float y, MonsterState state) {
        stateTime += Gdx.graphics.getDeltaTime();
        if (state == MonsterState.ATTACKING) {
            TextureRegion currentFrame = animations[state.ordinal()].getKeyFrame(stateTime, true);
            batch.draw(currentFrame, x, y, ATTACK_FRAME_WIDTH, ATTACK_FRAME_HEIGHT);
        } else {
            // Render other animations with default frame dimensions
            stateTime += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = animations[state.ordinal()].getKeyFrame(stateTime, true);
            batch.draw(currentFrame, x, y,32,32);
        }
    }
    public void renderDirection(SpriteBatch batch, float x, float y, Direction direction) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animations[direction.ordinal()].getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y, 32, 32);
    }

    public void dispose() {
        monsterSheet.dispose();// Dispose of the texture
    }
}
