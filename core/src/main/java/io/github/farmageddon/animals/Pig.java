package io.github.farmageddon.animals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.Items;

public class Pig extends Animal{
    public Pig(float x, float y, Stage stage, GameScreen gameScreen) {
        super(x, y, stage, gameScreen);
        favFood = Items.Item.CARROT;
        typeCanLay = false;
        typeCanBreed = true;
        setBound();
    }
    @Override
    public void initAnimation() {
        animalSheet = new Texture(Gdx.files.internal("Animals\\Pig\\Pig_01.png"));
        TextureRegion[][] tmpFrames = TextureRegion.split(animalSheet, frameWidth, frameHeight);
        animations = new Animation[10];
        animations[Activity.WALK_LEFT.ordinal()] = createAnimation(tmpFrames[1], 0, 8, 0.1f);
        animations[Activity.WALK_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[1], 0, 8, 0.1f);
        animations[Activity.SLEEP.ordinal()] = createAnimation(tmpFrames[7], 0, 3, 0.1f);
        animations[Activity.IDLE_LEFT.ordinal()] = createAnimation(tmpFrames[0], 0, 2, 0.3f);
        animations[Activity.IDLE_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[0], 0, 2, 0.3f);
        animations[Activity.HIT_LEFT.ordinal()] = createAnimation(tmpFrames[8], 0, 4, 0.1f);
        animations[Activity.HIT_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[8], 0, 4, 0.1f);
        stateTime = 0f;
    }
    @Override
    public void setBound() {
        minX = zoneManager.getPigZone().getX();
        minY = zoneManager.getPigZone().getY();
        maxX = zoneManager.getPigZone().getX() + zoneManager.getPigZone().getWidth();
        maxY = zoneManager.getPigZone().getY() + zoneManager.getPigZone().getHeight();
    }
}
