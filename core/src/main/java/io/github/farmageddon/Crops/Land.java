package io.github.farmageddon.Crops;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.DroppedItem;
import io.github.farmageddon.ultilites.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Land {
    private Crop crop;
    private GameScreen screen;
    public enum LandState {
        PLAIN,
        HOED,
        HOED_AND_WATERED,
    }

    private LandState currentState;
    private float timeSinceHoed;
    private static final float DRYING_TIME = 30f; // 5 seconds until hoed land dries

    private static Map<LandState, Texture> stateTextures;

    public static Texture deadPlantTexture;
//    private boolean hasDeadPlant = false;
    static {
        stateTextures = new HashMap<>();
//        stateTextures.put(LandState.PLAIN, new Texture("landtile/plain.png")); // Add texture for plain land
        stateTextures.put(LandState.HOED, new Texture("landtile/hoed.png"));
        stateTextures.put(LandState.HOED_AND_WATERED, new Texture("landtile/hoed_watered.png"));
        deadPlantTexture = new Texture("crops_die.png"); // Texture for dead plant
    }

//    public boolean isHasDeadPlant() {
//        return hasDeadPlant;
//    }
//    public void setHasDeadPlant(boolean hasDeadPlant) {
//        this.hasDeadPlant = hasDeadPlant;
//    }
    public Land() {
        currentState = LandState.PLAIN;
        timeSinceHoed = 0;
    }
    public boolean isEmpty() {
        return crop == null;
    }
    public void plantCrop(Crop newCrop) {
        this.crop = newCrop;
    }
    public Crop getCrop() {
        return crop;
    }
    public void removeCrop() {
        this.crop = null;
    }

    public DroppedItem harvestCrop() {
        if (crop != null && crop.getGrowthStage() == 3) {
            System.out.println(crop.getItemType());
            DroppedItem droppedItem = new DroppedItem(crop.getFrameSprite().getX(), crop.getFrameSprite().getY(), crop.getItemType(), Items.ItemType.FOOD);
            removeCrop();
            return droppedItem;
        }
        return null;
    }
    public void update(float delta) {
        if (currentState == LandState.HOED_AND_WATERED) {
            timeSinceHoed += delta;
            if (timeSinceHoed >= DRYING_TIME) {
                transitionTo(LandState.HOED);

            }
        } else if (currentState == LandState.HOED) {
            timeSinceHoed += delta;
            if (timeSinceHoed >= DRYING_TIME) {
                transitionTo(LandState.PLAIN);
                crop = null;
            }
        }
    }
    public void hoe() {
        if (currentState == LandState.PLAIN) {
            transitionTo(LandState.HOED);
        }
    }

    public void water() {
        if (currentState == LandState.HOED) {
            transitionTo(LandState.HOED_AND_WATERED);
        }
    }

//    public void plant() {
//        if (currentState == LandState.HOED_AND_WATERED) {
//            transitionTo(LandState.PLANTED);
//        }
//    }
//
//    public void harvest() {
//        if (currentState == LandState.PLANTED) {
//            transitionTo(LandState.PLAIN);
//        }
//    }

    private void transitionTo(LandState newState) {
        currentState = newState;
        if (newState == LandState.HOED) {
            timeSinceHoed = 0;
        }
    }

    public LandState getCurrentState() {
        return currentState;
    }
    public static Texture getTextureForState(LandState state) {
        return stateTextures.get(state);
    }

}
