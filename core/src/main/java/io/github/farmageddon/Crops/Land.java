package io.github.farmageddon.Crops;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class Land {
    private Crop crop;
    public enum LandState {
        PLAIN,
        HOED,
        HOED_AND_WATERED,
    }

    private LandState currentState;
    private float timeSinceHoed;
    private static final float DRYING_TIME = 5.0f; // 5 seconds until hoed land dries

    private static Map<LandState, Texture> stateTextures;

    static {
        stateTextures = new HashMap<>();
        stateTextures.put(LandState.HOED, new Texture("landtile/hoed.png"));
        stateTextures.put(LandState.HOED_AND_WATERED, new Texture("landtile/hoed_watered.png"));
    }
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
    public int harvestCrop() {
        if (crop != null && crop.getGrowthStage() == 3) {
            int price = crop.getPrice(); // Get the crop's price as a reward
            removeCrop(); // Remove the harvested crop
            return price;
        }
        return 0; // No harvestable crop
    }
    public void update(float delta) {
        if (currentState == LandState.HOED) {
            timeSinceHoed += delta;
            if (timeSinceHoed >= DRYING_TIME) {
                transitionTo(LandState.PLAIN);
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
