package io.github.farmageddon.ultilites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.ultilites.Items;

public class DroppedItem {
    private Vector2 position;
    private Texture texture;

    public Texture getTexture() {
        return texture;
    }

    private Rectangle bounds;
    private Items.Item itemType;
    private int tileWidth = 16;
    private int tileHeight = 16;
    private Items.ItemType type;
    public Items.ItemType getType() {
        return type;
    }
    public DroppedItem(float x, float y, Items.Item item, Items.ItemType type) {
        this.position = new Vector2(x, y);
        this.itemType = item;
        this.type = type;
        // Determine texture based on the item type
        String texturePath = getTexturePathForItem(item,type);
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.bounds = new Rectangle(x, y, 16, 16);
    }

    private String getTexturePathForItem(Items.Item item, Items.ItemType type) {
        switch (type) {
            case FOOD:
                return "foodicon/" + item.toString().toLowerCase() + "item.png";
            default:
                // Default to crop-related textures
                return "croptile/" + item.toString().toLowerCase() + "item.png";
        }
    }

    public void render(SpriteBatch batch) {
        System.out.println("hehe");
        batch.draw(texture, position.x, position.y, tileWidth, tileHeight);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Items.Item getItemType() {
        return itemType;
    }
    public void dispose() {
        texture.dispose();
    }
}
