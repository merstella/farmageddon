package io.github.farmageddon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Animator {

    private final Texture playerSheet;
    private final Texture torchSheet;
    private final Texture playerActionSheet;
    private final Texture playerFishingSheet;
    private final Texture monsterSheet;
    private final Texture bowmanSheet;
    private final Texture bossSheet;
    private final Texture mageSheet;
    private final Animation<TextureRegion>[] animations;
    public static Animation<TextureRegion>[] actionAnimations;
    public Animation<TextureRegion>[] monsterAnimations;
    public Animation<TextureRegion>[] bowmanAnimations;
    public Animation<TextureRegion>[] bossAnimations;
    public Animation<TextureRegion>[] bossmageAnimations;
    public enum Direction {
        UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT,
        IDLE_UP, IDLE_RIGHT, IDLE_DOWN, IDLE_LEFT, IDLE_DOWN_RIGHT, IDLE_DOWN_LEFT,
        IDLE_UP_RIGHT, WALK, SLEEP, IDLE, IDLE_UP_LEFT,
        TORCH_UP, TORCH_UP_RIGHT, TORCH_RIGHT, TORCH_DOWN_RIGHT, TORCH_DOWN, TORCH_DOWN_LEFT, TORCH_LEFT, TORCH_UP_LEFT,
        IDLE_TORCH_UP, IDLE_TORCH_RIGHT, IDLE_TORCH_DOWN, IDLE_TORCH_LEFT, IDLE_TORCH_DOWN_RIGHT, IDLE_TORCH_DOWN_LEFT,
        IDLE_TORCH_UP_RIGHT, IDLE_TORCH_UP_LEFT,
    }

    public enum Activity {
        NONE, // Default state when no activity
        HOE_UP, HOE_DOWN, HOE_LEFT, HOE_RIGHT,
        WATER_UP, WATER_DOWN, WATER_LEFT, WATER_RIGHT, START_FISHING_RIGHT, START_FISHING_LEFT,
        WAIT_FISHING_RIGHT, WAIT_FISHING_LEFT , DONE_FISHING_RIGHT, DONE_FISHING_LEFT,
        ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT, ATTACK_UP,
        HIT_UP, HIT_UP_RIGHT, HIT_RIGHT, HIT_DOWN_RIGHT, HIT_DOWN, HIT_DOWN_LEFT, HIT_LEFT, HIT_UP_LEFT,
    }
    public enum MonsterActivity {
        IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT, IDLE_UP, IDLE_UP_LEFT, IDLE_UP_RIGHT, IDLE_DOWN_RIGHT, IDLE_DOWN_LEFT,
        UP, LEFT, DOWN, RIGHT, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT,
        DEAD,
        ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT, ATTACK_UP, ATTACK_UP_LEFT, ATTACK_UP_RIGHT, ATTACK_DOWN_RIGHT, ATTACK_DOWN_LEFT,
        HIT_UP, HIT_DOWN, HIT_LEFT, HIT_RIGHT, HIT_UP_LEFT, HIT_UP_RIGHT, HIT_DOWN_RIGHT, HIT_DOWN_LEFT
    }

    public enum BossMangeActivity {
        IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT, IDLE_UP, IDLE_UP_LEFT, IDLE_UP_RIGHT, IDLE_DOWN_RIGHT, IDLE_DOWN_LEFT,
        UP, LEFT, DOWN, RIGHT, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT,
        DEAD,
        ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT, ATTACK_UP, ATTACK_UP_LEFT, ATTACK_UP_RIGHT, ATTACK_DOWN_RIGHT, ATTACK_DOWN_LEFT,
        HIT_UP, HIT_DOWN, HIT_LEFT, HIT_RIGHT, HIT_UP_LEFT, HIT_UP_RIGHT, HIT_DOWN_RIGHT, HIT_DOWN_LEFT
    }

    public enum BossActivity {
        IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT, IDLE_UP, IDLE_UP_LEFT, IDLE_UP_RIGHT, IDLE_DOWN_RIGHT, IDLE_DOWN_LEFT,
        UP, LEFT, DOWN, RIGHT, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT,
        DEAD,
        ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT, ATTACK_UP, ATTACK_UP_LEFT, ATTACK_UP_RIGHT, ATTACK_DOWN_RIGHT, ATTACK_DOWN_LEFT,
        HIT_UP, HIT_DOWN, HIT_LEFT, HIT_RIGHT, HIT_UP_LEFT, HIT_UP_RIGHT, HIT_DOWN_RIGHT, HIT_DOWN_LEFT
    }

    public enum BowmanActivity {
        IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT, IDLE_UP, IDLE_UP_LEFT, IDLE_UP_RIGHT, IDLE_DOWN_RIGHT, IDLE_DOWN_LEFT,
        UP, LEFT, DOWN, RIGHT, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT,
        DEAD,
        ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT, ATTACK_UP, ATTACK_UP_LEFT, ATTACK_UP_RIGHT, ATTACK_DOWN_RIGHT, ATTACK_DOWN_LEFT,
        HIT_UP, HIT_DOWN, HIT_LEFT, HIT_RIGHT, HIT_UP_LEFT, HIT_UP_RIGHT, HIT_DOWN_RIGHT, HIT_DOWN_LEFT
    }

    public Animator() {

        // Load textures and initialize animations as before
        playerSheet = new Texture(Gdx.files.internal("player.png"));
        torchSheet = new Texture(Gdx.files.internal("Player/PLayer_Old/Player_Holding_Torch_Lantern.png"));
        playerActionSheet = new Texture(Gdx.files.internal("Player/Player_Actions.png"));
        playerFishingSheet = new Texture(Gdx.files.internal("Player\\Player_Fishing.png"));
        monsterSheet = new Texture(Gdx.files.internal("Enemies/Skeleton/Skeleton_Swordman.png"));
        bowmanSheet = new Texture(Gdx.files.internal("Enemies/Skeleton/Skeleton_Bowman.png"));
        bossSheet = new Texture(Gdx.files.internal("Enemies/Skeleton/Boss_Swordman.png"));
        mageSheet = new Texture(Gdx.files.internal("Enemies/Skeleton/Boss_Swordman.png"));
        // Define the frame dimensions and split frames (same as original)
        int frameWidth = 32, frameHeight = 32;
        int frameFishingWidth = playerFishingSheet.getWidth() / 9; int frameFishingHeight = playerFishingSheet.getHeight() / 8;
        int frameActionWidth = 48, frameActionHeight = 48;
        TextureRegion[][] tmpFrames = TextureRegion.split(playerSheet, frameWidth, frameHeight);
        TextureRegion[][] tmpFrames2 = TextureRegion.split(playerActionSheet, frameActionWidth, frameActionHeight);
        TextureRegion[][] tmpFrames3 = TextureRegion.split(playerFishingSheet, frameFishingWidth, frameFishingHeight);


        TextureRegion[][] tmpFrames4 = TextureRegion.split(monsterSheet, 32, 32);
        TextureRegion[][] tmpFrames7 = TextureRegion.split(bowmanSheet, 32, 32);
        TextureRegion[][] tmpFrames5 = TextureRegion.split(new Texture(Gdx.files.internal("Enemies/Skeleton/Skeleton_Swordman_Attack.png")), 64, 64);
        TextureRegion[][] tmpFrames6 = TextureRegion.split(torchSheet, 32, 32);
        TextureRegion[][] tmpFrames8 = TextureRegion.split(bossSheet, 32, 32);
        TextureRegion[][] tmpFrames9 = TextureRegion.split(new Texture(Gdx.files.internal("Enemies/Skeleton/Boss_Swordman_Attack.png")), 64, 64);
        TextureRegion[][] tmpFrames10 = TextureRegion.split(mageSheet, 32, 32);

        animations = new Animation[Direction.values().length];
        actionAnimations = new Animation[Activity.values().length];
        monsterAnimations = new Animation[MonsterActivity.values().length];
        bowmanAnimations = new Animation[BowmanActivity.values().length];
        bossAnimations = new Animation[BossActivity.values().length];
        bossmageAnimations = new Animation[BossMangeActivity.values().length];

        // Create animations for each direction
        animations[Direction.UP.ordinal()] = createAnimation(tmpFrames[5], 0, 6); // Assuming 4 frames in the first row for UP
        animations[Direction.UP_RIGHT.ordinal()] = createAnimation(tmpFrames[4], 0, 6); // Adjust indices based on layout
        animations[Direction.RIGHT.ordinal()] = createAnimation(tmpFrames[4], 0, 6);
        animations[Direction.DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames[4], 0, 6);
        animations[Direction.DOWN.ordinal()] = createAnimation(tmpFrames[3], 0, 6);
        animations[Direction.DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[4], 0, 6);
        animations[Direction.LEFT.ordinal()] = createFlippedAnimation(tmpFrames[4], 0, 6);
        animations[Direction.UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[4], 0, 6);

        animations[Direction.IDLE_UP.ordinal()] = createAnimation(tmpFrames[2], 0, 6);
        animations[Direction.IDLE_RIGHT.ordinal()] = createAnimation(tmpFrames[1], 0, 6);
        animations[Direction.IDLE_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[1], 0, 6);
        animations[Direction.IDLE_DOWN.ordinal()] = createFlippedAnimation(tmpFrames[0], 0, 6);
        animations[Direction.IDLE_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames[4], 0, 1);
        animations[Direction.IDLE_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[1], 0, 6);
        animations[Direction.IDLE_UP_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[4], 0, 1);
        animations[Direction.IDLE_UP_LEFT.ordinal()] = createAnimation(tmpFrames[4], 0, 1);

        animations[Direction.TORCH_UP.ordinal()] = createAnimation(tmpFrames6[5], 0, 6); // Assuming 4 frames in the first row for UP
        animations[Direction.TORCH_UP_RIGHT.ordinal()] = createAnimation(tmpFrames6[4], 0, 6); // Adjust indices based on layout
        animations[Direction.TORCH_RIGHT.ordinal()] = createAnimation(tmpFrames6[4], 0, 6);
        animations[Direction.TORCH_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames6[4], 0, 6);
        animations[Direction.TORCH_DOWN.ordinal()] = createAnimation(tmpFrames6[3], 0, 6);
        animations[Direction.TORCH_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames6[4], 0, 6);
        animations[Direction.TORCH_LEFT.ordinal()] = createFlippedAnimation(tmpFrames6[4], 0, 6);
        animations[Direction.TORCH_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames6[4], 0, 6);

        animations[Direction.IDLE_TORCH_UP.ordinal()] = createAnimation(tmpFrames6[2], 0, 6);
        animations[Direction.IDLE_TORCH_RIGHT.ordinal()] = createAnimation(tmpFrames6[1], 0, 6);
        animations[Direction.IDLE_TORCH_LEFT.ordinal()] = createFlippedAnimation(tmpFrames6[1], 0, 6);
        animations[Direction.IDLE_TORCH_DOWN.ordinal()] = createFlippedAnimation(tmpFrames6[0], 0, 6);
        animations[Direction.IDLE_TORCH_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames6[4], 0, 1);
        animations[Direction.IDLE_TORCH_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames6[1], 0, 6);
        animations[Direction.IDLE_TORCH_UP_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames6[4], 0, 1);
        animations[Direction.IDLE_TORCH_UP_LEFT.ordinal()] = createAnimation(tmpFrames6[4], 0, 1);

        actionAnimations[Activity.HOE_UP.ordinal()] = createAnimation(tmpFrames2[8], 0, 6);
        actionAnimations[Activity.HOE_DOWN.ordinal()] = createAnimation(tmpFrames2[7], 0, 6);
        actionAnimations[Activity.HOE_LEFT.ordinal()] = createFlippedAnimation(tmpFrames2[6], 0, 6);
        actionAnimations[Activity.HOE_RIGHT.ordinal()] = createAnimation(tmpFrames2[6], 0, 6);
        actionAnimations[Activity.WATER_UP.ordinal()] = createAnimation(tmpFrames2[10], 0, 6);
        actionAnimations[Activity.WATER_DOWN.ordinal()] = createAnimation(tmpFrames2[9], 0, 6);
        actionAnimations[Activity.WATER_LEFT.ordinal()] = createFlippedAnimation(tmpFrames2[11], 0, 6);
        actionAnimations[Activity.WATER_RIGHT.ordinal()] = createAnimation(tmpFrames2[11], 0, 6);
        actionAnimations[Activity.ATTACK_DOWN.ordinal()] = createAnimation(tmpFrames[6], 0, 4);
        actionAnimations[Activity.ATTACK_RIGHT.ordinal()] = createAnimation(tmpFrames[9], 0, 4);
        actionAnimations[Activity.ATTACK_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[9], 0, 4);
        actionAnimations[Activity.ATTACK_UP.ordinal()] = createFlippedAnimation(tmpFrames[12], 0, 4);
        actionAnimations[Activity.START_FISHING_RIGHT.ordinal()] = createAnimation(tmpFrames3[0], 0, 9);
        actionAnimations[Activity.START_FISHING_LEFT.ordinal()] = createFlippedAnimation(tmpFrames3[0], 0, 9);
        actionAnimations[Activity.WAIT_FISHING_RIGHT.ordinal()] = createAnimation(tmpFrames3[1], 0, 4);
        actionAnimations[Activity.WAIT_FISHING_LEFT.ordinal()] = createFlippedAnimation(tmpFrames3[1], 0, 4);
        actionAnimations[Activity.DONE_FISHING_RIGHT.ordinal()] = createAnimation(tmpFrames3[2], 0, 8);
        actionAnimations[Activity.DONE_FISHING_LEFT.ordinal()] = createFlippedAnimation(tmpFrames3[2], 0, 8);

        actionAnimations[Activity.HIT_DOWN.ordinal()] = createAnimation(tmpFrames[16], 0, 4);
        actionAnimations[Activity.HIT_RIGHT.ordinal()] = createAnimation(tmpFrames[17], 0, 4);
        actionAnimations[Activity.HIT_UP.ordinal()] = createAnimation(tmpFrames[18], 0, 4);
        actionAnimations[Activity.HIT_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[17], 0, 4);
        actionAnimations[Activity.HIT_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames[17], 0, 4);
        actionAnimations[Activity.HIT_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[17], 0, 4);
        actionAnimations[Activity.HIT_UP_RIGHT.ordinal()] = createAnimation(tmpFrames[17], 0, 4);
        actionAnimations[Activity.HIT_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[17], 0, 4);

        monsterAnimations[MonsterActivity.IDLE_DOWN.ordinal()] = createAnimation(tmpFrames4[0], 0, 6);
        monsterAnimations[MonsterActivity.IDLE_LEFT.ordinal()] = createFlippedAnimation(tmpFrames4[1], 0, 6);
        monsterAnimations[MonsterActivity.IDLE_RIGHT.ordinal()] = createAnimation(tmpFrames4[1], 0, 6);
        monsterAnimations[MonsterActivity.IDLE_UP.ordinal()] = createAnimation(tmpFrames4[2], 0, 6);
        monsterAnimations[MonsterActivity.IDLE_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames4[1], 0, 6);
        monsterAnimations[MonsterActivity.IDLE_UP_RIGHT.ordinal()] = createAnimation(tmpFrames4[1], 0, 6);
        monsterAnimations[MonsterActivity.IDLE_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames4[1], 0, 6);
        monsterAnimations[MonsterActivity.IDLE_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames4[1], 0, 6);
        monsterAnimations[MonsterActivity.UP.ordinal()] = createAnimation(tmpFrames4[5], 0, 6);
        monsterAnimations[MonsterActivity.LEFT.ordinal()] = createFlippedAnimation(tmpFrames4[4], 0, 6);
        monsterAnimations[MonsterActivity.DOWN.ordinal()] = createAnimation(tmpFrames4[3], 0, 6);
        monsterAnimations[MonsterActivity.RIGHT.ordinal()] = createAnimation(tmpFrames4[4], 0, 6);
        monsterAnimations[MonsterActivity.UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames4[4], 0, 6);
        monsterAnimations[MonsterActivity.UP_RIGHT.ordinal()] = createAnimation(tmpFrames4[4], 0, 6);
        monsterAnimations[MonsterActivity.DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames4[4], 0, 6);
        monsterAnimations[MonsterActivity.DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames4[4], 0, 6);
        monsterAnimations[MonsterActivity.DEAD.ordinal()] = createAnimation(tmpFrames4[6], 0, 5, 0.15f);
        monsterAnimations[MonsterActivity.ATTACK_DOWN.ordinal()] = createAnimation(tmpFrames5[0], 0, 4);
        monsterAnimations[MonsterActivity.ATTACK_LEFT.ordinal()] = createFlippedAnimation(tmpFrames5[1], 0, 4);
        monsterAnimations[MonsterActivity.ATTACK_RIGHT.ordinal()] = createAnimation(tmpFrames5[1], 0, 4);
        monsterAnimations[MonsterActivity.ATTACK_UP.ordinal()] = createAnimation(tmpFrames5[2], 0, 4);
        monsterAnimations[MonsterActivity.ATTACK_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames5[1], 0, 4);
        monsterAnimations[MonsterActivity.ATTACK_UP_RIGHT.ordinal()] = createAnimation(tmpFrames5[1], 0, 4);
        monsterAnimations[MonsterActivity.ATTACK_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames5[1], 0, 4);
        monsterAnimations[MonsterActivity.ATTACK_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames5[1], 0, 4);
        monsterAnimations[MonsterActivity.HIT_UP.ordinal()] = createAnimation(tmpFrames4[15], 0, 4);
        monsterAnimations[MonsterActivity.HIT_DOWN.ordinal()] = createAnimation(tmpFrames4[13], 0, 4);
        monsterAnimations[MonsterActivity.HIT_LEFT.ordinal()] = createFlippedAnimation(tmpFrames4[14], 0, 4);
        monsterAnimations[MonsterActivity.HIT_RIGHT.ordinal()] = createAnimation(tmpFrames4[14], 0, 4);
        monsterAnimations[MonsterActivity.HIT_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames4[14], 0, 4);
        monsterAnimations[MonsterActivity.HIT_UP_RIGHT.ordinal()] = createAnimation(tmpFrames4[14], 0, 4);
        monsterAnimations[MonsterActivity.HIT_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames4[14], 0, 4);
        monsterAnimations[MonsterActivity.HIT_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames4[14], 0, 4);

        bowmanAnimations[MonsterActivity.IDLE_DOWN.ordinal()] = createAnimation(tmpFrames7[0], 0, 6);
        bowmanAnimations[MonsterActivity.IDLE_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[1], 0, 6);
        bowmanAnimations[MonsterActivity.IDLE_RIGHT.ordinal()] = createAnimation(tmpFrames7[1], 0, 6);
        bowmanAnimations[MonsterActivity.IDLE_UP.ordinal()] = createAnimation(tmpFrames7[2], 0, 6);
        bowmanAnimations[MonsterActivity.IDLE_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[1], 0, 6);// IDLE_LEFT
        bowmanAnimations[MonsterActivity.IDLE_UP_RIGHT.ordinal()] = createAnimation(tmpFrames7[1], 0, 6);//IDLE_RIGHT
        bowmanAnimations[MonsterActivity.IDLE_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames7[1], 0, 6);//IDLE_RIGHT
        bowmanAnimations[MonsterActivity.IDLE_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[1], 0, 6);//IDLE_LEFT
        bowmanAnimations[MonsterActivity.UP.ordinal()] = createAnimation(tmpFrames7[5], 0, 6);
        bowmanAnimations[MonsterActivity.LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[4], 0, 6);
        bowmanAnimations[MonsterActivity.DOWN.ordinal()] = createAnimation(tmpFrames7[3], 0, 6);
        bowmanAnimations[MonsterActivity.RIGHT.ordinal()] = createAnimation(tmpFrames7[4], 0, 6);
        bowmanAnimations[MonsterActivity.UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[4], 0, 6);
        bowmanAnimations[MonsterActivity.UP_RIGHT.ordinal()] = createAnimation(tmpFrames7[4], 0, 6);
        bowmanAnimations[MonsterActivity.DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames7[4], 0, 6);
        bowmanAnimations[MonsterActivity.DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[4], 0, 6);
        bowmanAnimations[MonsterActivity.DEAD.ordinal()] = createAnimation(tmpFrames7[9], 0, 5, 0.15f);
        bowmanAnimations[MonsterActivity.ATTACK_DOWN.ordinal()] = createAnimation(tmpFrames7[6], 0, 6);
        bowmanAnimations[MonsterActivity.ATTACK_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[7], 0, 6);
        bowmanAnimations[MonsterActivity.ATTACK_RIGHT.ordinal()] = createAnimation(tmpFrames7[7], 0, 6);
        bowmanAnimations[MonsterActivity.ATTACK_UP.ordinal()] = createAnimation(tmpFrames7[8], 0, 6);
        bowmanAnimations[MonsterActivity.ATTACK_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[7], 0, 6);
        bowmanAnimations[MonsterActivity.ATTACK_UP_RIGHT.ordinal()] = createAnimation(tmpFrames7[7], 0, 6);
        bowmanAnimations[MonsterActivity.ATTACK_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames7[7], 0, 6);
        bowmanAnimations[MonsterActivity.ATTACK_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[7], 0, 6);
        bowmanAnimations[MonsterActivity.HIT_UP.ordinal()] = createAnimation(tmpFrames7[12], 0, 4);
        bowmanAnimations[MonsterActivity.HIT_DOWN.ordinal()] = createAnimation(tmpFrames7[10], 0, 4);
        bowmanAnimations[MonsterActivity.HIT_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[11], 0, 4);
        bowmanAnimations[MonsterActivity.HIT_RIGHT.ordinal()] = createAnimation(tmpFrames7[11], 0, 4);
        bowmanAnimations[MonsterActivity.HIT_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[11], 0, 4);
        bowmanAnimations[MonsterActivity.HIT_UP_RIGHT.ordinal()] = createAnimation(tmpFrames7[11], 0, 4);
        bowmanAnimations[MonsterActivity.HIT_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames7[11], 0, 4);
        bowmanAnimations[MonsterActivity.HIT_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames7[11], 0, 4);

        bossAnimations[MonsterActivity.IDLE_DOWN.ordinal()] = createAnimation(tmpFrames8[0], 0, 6);
        bossAnimations[MonsterActivity.IDLE_LEFT.ordinal()] = createFlippedAnimation(tmpFrames8[1], 0, 6);
        bossAnimations[MonsterActivity.IDLE_RIGHT.ordinal()] = createAnimation(tmpFrames8[1], 0, 6);
        bossAnimations[MonsterActivity.IDLE_UP.ordinal()] = createAnimation(tmpFrames8[2], 0, 6);
        bossAnimations[MonsterActivity.IDLE_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames8[1], 0, 6);
        bossAnimations[MonsterActivity.IDLE_UP_RIGHT.ordinal()] = createAnimation(tmpFrames8[1], 0, 6);
        bossAnimations[MonsterActivity.IDLE_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames8[1], 0, 6);
        bossAnimations[MonsterActivity.IDLE_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames8[1], 0, 6);
        bossAnimations[MonsterActivity.UP.ordinal()] = createAnimation(tmpFrames8[5], 0, 6);
        bossAnimations[MonsterActivity.LEFT.ordinal()] = createFlippedAnimation(tmpFrames8[4], 0, 6);
        bossAnimations[MonsterActivity.DOWN.ordinal()] = createAnimation(tmpFrames8[3], 0, 6);
        bossAnimations[MonsterActivity.RIGHT.ordinal()] = createAnimation(tmpFrames8[4], 0, 6);
        bossAnimations[MonsterActivity.UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames8[4], 0, 6);
        bossAnimations[MonsterActivity.UP_RIGHT.ordinal()] = createAnimation(tmpFrames8[4], 0, 6);
        bossAnimations[MonsterActivity.DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames8[4], 0, 6);
        bossAnimations[MonsterActivity.DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames8[4], 0, 6);
        bossAnimations[MonsterActivity.DEAD.ordinal()] = createAnimation(tmpFrames8[6], 0, 5, 0.15f);
        bossAnimations[MonsterActivity.ATTACK_DOWN.ordinal()] = createAnimation(tmpFrames9[0], 0, 4);
        bossAnimations[MonsterActivity.ATTACK_LEFT.ordinal()] = createFlippedAnimation(tmpFrames9[1], 0, 4);
        bossAnimations[MonsterActivity.ATTACK_RIGHT.ordinal()] = createAnimation(tmpFrames9[1], 0, 4);
        bossAnimations[MonsterActivity.ATTACK_UP.ordinal()] = createAnimation(tmpFrames9[2], 0, 4);
        bossAnimations[MonsterActivity.ATTACK_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames9[1], 0, 4);
        bossAnimations[MonsterActivity.ATTACK_UP_RIGHT.ordinal()] = createAnimation(tmpFrames9[1], 0, 4);
        bossAnimations[MonsterActivity.ATTACK_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames9[1], 0, 4);
        bossAnimations[MonsterActivity.ATTACK_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames9[1], 0, 4);
        bossAnimations[MonsterActivity.HIT_UP.ordinal()] = createAnimation(tmpFrames8[15], 0, 4);
        bossAnimations[MonsterActivity.HIT_DOWN.ordinal()] = createAnimation(tmpFrames8[13], 0, 4);
        bossAnimations[MonsterActivity.HIT_LEFT.ordinal()] = createFlippedAnimation(tmpFrames8[14], 0, 4);
        bossAnimations[MonsterActivity.HIT_RIGHT.ordinal()] = createAnimation(tmpFrames8[14], 0, 4);
        bossAnimations[MonsterActivity.HIT_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames8[14], 0, 4);
        bossAnimations[MonsterActivity.HIT_UP_RIGHT.ordinal()] = createAnimation(tmpFrames8[14], 0, 4);
        bossAnimations[MonsterActivity.HIT_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames8[14], 0, 4);
        bossAnimations[MonsterActivity.HIT_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames8[14], 0, 4);

        bossmageAnimations[MonsterActivity.IDLE_DOWN.ordinal()] = createAnimation(tmpFrames10[0], 0, 6);
        bossmageAnimations[MonsterActivity.IDLE_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[1], 0, 6);
        bossmageAnimations[MonsterActivity.IDLE_RIGHT.ordinal()] = createAnimation(tmpFrames10[1], 0, 6);
        bossmageAnimations[MonsterActivity.IDLE_UP.ordinal()] = createAnimation(tmpFrames10[2], 0, 6);
        bossmageAnimations[MonsterActivity.IDLE_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[1], 0, 6);// IDLE_LEFT
        bossmageAnimations[MonsterActivity.IDLE_UP_RIGHT.ordinal()] = createAnimation(tmpFrames10[1], 0, 6);//IDLE_RIGHT
        bossmageAnimations[MonsterActivity.IDLE_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames10[1], 0, 6);//IDLE_RIGHT
        bossmageAnimations[MonsterActivity.IDLE_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[1], 0, 6);//IDLE_LEFT
        bossmageAnimations[MonsterActivity.UP.ordinal()] = createAnimation(tmpFrames10[5], 0, 6);
        bossmageAnimations[MonsterActivity.LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[4], 0, 6);
        bossmageAnimations[MonsterActivity.DOWN.ordinal()] = createAnimation(tmpFrames10[3], 0, 6);
        bossmageAnimations[MonsterActivity.RIGHT.ordinal()] = createAnimation(tmpFrames10[4], 0, 6);
        bossmageAnimations[MonsterActivity.UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[4], 0, 6);
        bossmageAnimations[MonsterActivity.UP_RIGHT.ordinal()] = createAnimation(tmpFrames10[4], 0, 6);
        bossmageAnimations[MonsterActivity.DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames10[4], 0, 6);
        bossmageAnimations[MonsterActivity.DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[4], 0, 6);
        bossmageAnimations[MonsterActivity.DEAD.ordinal()] = createAnimation(tmpFrames10[9], 0, 5, 0.15f);
        bossmageAnimations[MonsterActivity.ATTACK_DOWN.ordinal()] = createAnimation(tmpFrames10[6], 0, 8);
        bossmageAnimations[MonsterActivity.ATTACK_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[7], 0, 8);
        bossmageAnimations[MonsterActivity.ATTACK_RIGHT.ordinal()] = createAnimation(tmpFrames10[7], 0, 8);
        bossmageAnimations[MonsterActivity.ATTACK_UP.ordinal()] = createAnimation(tmpFrames10[8], 0, 8);
        bossmageAnimations[MonsterActivity.ATTACK_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[7], 0, 8);
        bossmageAnimations[MonsterActivity.ATTACK_UP_RIGHT.ordinal()] = createAnimation(tmpFrames10[7], 0, 8);
        bossmageAnimations[MonsterActivity.ATTACK_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames10[7], 0, 8);
        bossmageAnimations[MonsterActivity.ATTACK_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[7], 0, 8);
        bossmageAnimations[MonsterActivity.HIT_UP.ordinal()] = createAnimation(tmpFrames10[12], 0, 4);
        bossmageAnimations[MonsterActivity.HIT_DOWN.ordinal()] = createAnimation(tmpFrames10[10], 0, 4);
        bossmageAnimations[MonsterActivity.HIT_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[11], 0, 4);
        bossmageAnimations[MonsterActivity.HIT_RIGHT.ordinal()] = createAnimation(tmpFrames10[11], 0, 4);
        bossmageAnimations[MonsterActivity.HIT_UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[11], 0, 4);
        bossmageAnimations[MonsterActivity.HIT_UP_RIGHT.ordinal()] = createAnimation(tmpFrames10[11], 0, 4);
        bossmageAnimations[MonsterActivity.HIT_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames10[11], 0, 4);
        bossmageAnimations[MonsterActivity.HIT_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames10[11], 0, 4);
    }
    private Animation<TextureRegion> createAnimation(TextureRegion[] frames, int startFrame, int frameCount) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        System.arraycopy(frames, startFrame, directionFrames, 0, frameCount);
        return new Animation<>(0.1f, directionFrames);
    }
    private Animation<TextureRegion> createAnimation(TextureRegion[] frames, int startFrame, int frameCount, float frameDuration) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        System.arraycopy(frames, startFrame, directionFrames, 0, frameCount);
        return new Animation<>(frameDuration, directionFrames);
    }
    private Animation<TextureRegion> createFlippedAnimation(TextureRegion[] frames, int startFrame, int frameCount) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            directionFrames[i] = new TextureRegion(frames[startFrame + i]);
            directionFrames[i].flip(true, false); // Flip horizontally
        }
        return new Animation<>(0.1f, directionFrames);
    }
    private Animation<TextureRegion> createFlippedAnimation(TextureRegion[] frames, int startFrame, int frameCount, float frameDuration) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            directionFrames[i] = new TextureRegion(frames[startFrame + i]);
            directionFrames[i].flip(true, false); // Flip horizontally
        }
        return new Animation<>(frameDuration, directionFrames);
    }
    // Remove or modify this method
    public void render(SpriteBatch batch, float x, float y, Direction direction, float stateTime) {

        // Get the current frame of the animation for the specified direction
        TextureRegion currentFrame = animations[direction.ordinal()].getKeyFrame(stateTime, true);
        // Draw the current frame at the specified position
//        System.out.println(2);
        batch.draw(currentFrame, x, y, Player.WIDTH , Player.HEIGHT);


    }
    public void render(SpriteBatch batch, float x, float y, MonsterActivity monsterActivity,  float stateTime) {
        // Get the current frame of the animation for the specified direction
        TextureRegion currentFrame = monsterAnimations[monsterActivity.ordinal()].getKeyFrame(stateTime, true);
        if (monsterActivity == MonsterActivity.DEAD) {
            currentFrame = monsterAnimations[monsterActivity.ordinal()].getKeyFrame(stateTime, true);
        }
        // Draw the current frame at the specified position
        if (monsterActivity == MonsterActivity.ATTACK_DOWN ||
        monsterActivity == MonsterActivity.ATTACK_DOWN_LEFT || monsterActivity == MonsterActivity.ATTACK_LEFT
            || monsterActivity == MonsterActivity.ATTACK_RIGHT || monsterActivity == MonsterActivity.ATTACK_UP ||
            monsterActivity == MonsterActivity.ATTACK_UP_RIGHT || monsterActivity == MonsterActivity.ATTACK_DOWN_RIGHT ||
            monsterActivity == MonsterActivity.ATTACK_UP_LEFT) {

            batch.draw(currentFrame, x - 16, y - 16, 64, 64);
        }
        else {
            batch.draw(currentFrame, x, y, 32, 32);
        }
    }
    public void renderActivity(SpriteBatch batch, float x, float y, Activity activity,  float stateTime) {
        if (activity == Activity.NONE) return;
        Animation<TextureRegion> activityAnimation = actionAnimations[activity.ordinal()];
        if (activity == Activity.ATTACK_DOWN || activity == Activity.ATTACK_UP || activity == Activity.ATTACK_LEFT || activity == Activity.ATTACK_RIGHT) {
            TextureRegion currentFrame = activityAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, x, y , 32, 32);
            return;
        }
        if (activity.toString().contains("HIT")) {

            TextureRegion currentFrame = activityAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, x, y, 32, 32);
            return;
        }
        if (activity == Activity.WAIT_FISHING_RIGHT) {
            // Change to true to loop the animation
            TextureRegion currentFrame = activityAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, x - 8, y - 8, 48, 48);
        }
         else {
            TextureRegion currentFrame = activityAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, x - 8, y - 8, 48, 48);
        }
    }

    public void dispose() {
        playerSheet.dispose();
        playerActionSheet.dispose();
        playerFishingSheet.dispose();
        monsterSheet.dispose();
    }
}
