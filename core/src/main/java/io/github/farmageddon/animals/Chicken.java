package io.github.farmageddon.animals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.farmageddon.ultilites.DroppedItem;
import io.github.farmageddon.ultilites.ItemList;
import io.github.farmageddon.ultilites.Items;

public class Chicken extends Animal{

    public Chicken(float x, float y, Stage stage) {
        super(x, y, stage);
        droppedFood = new DroppedItem(x, y, Items.Item.CHICKEN_MEAT, Items.ItemType.FOOD);
        typeCanLay = true;
        typeCanBreed = false;
        setBound();
    }
    @Override
    public void initAnimation() {
        animalSheet = new Texture(Gdx.files.internal("Animals\\Chicken\\Chicken_01.png"));
        TextureRegion[][] tmpFrames = TextureRegion.split(animalSheet, frameWidth, frameHeight);
        animations = new Animation[10];
        animations[Activity.WALK_LEFT.ordinal()] = createAnimation(tmpFrames[1], 0, 6, 0.1f);
        animations[Activity.WALK_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[1], 0, 6, 0.1f);
        animations[Activity.SLEEP.ordinal()] = createAnimation(tmpFrames[6], 0, 8, 0.1f);
        animations[Activity.IDLE_LEFT.ordinal()] = createAnimation(tmpFrames[0], 0, 2, 0.3f);
        animations[Activity.IDLE_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[0], 0, 2, 0.3f);
        animations[Activity.HIT_LEFT.ordinal()] = createAnimation(tmpFrames[7], 0, 4, 0.1f);
        animations[Activity.HIT_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[7], 0, 4, 0.1f);
        stateTime = 0f;
    }
    @Override
    public void setBound() {
        minX = zoneManager.getChickenZone().getX();
        minY = zoneManager.getChickenZone().getY();
        maxX = zoneManager.getChickenZone().getX() + zoneManager.getChickenZone().getWidth();
        maxY = zoneManager.getChickenZone().getY() + zoneManager.getChickenZone().getHeight();
    }
}
