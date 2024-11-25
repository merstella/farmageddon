package io.github.farmageddon.markets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Items {
    private String itemName;
    private Texture texture;
    public int Cost;

    public Items(String itemName, Texture texture, int Cost) {
        this.itemName = itemName;
        this.texture = texture;
        this.Cost = Cost;
    }

    public void render(SpriteBatch batch, float x, float y) {
        batch.draw(texture, x, y);
    }

    public String getItemName() {
        return itemName;
    }

    public int getCost() {
        return Cost;
    }

    public Texture getTexture() {
        return texture;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

}
