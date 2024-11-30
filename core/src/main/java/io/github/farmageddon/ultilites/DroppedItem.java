package io.github.farmageddon.ultilites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DroppedItem {
    private Vector2 position;
    private Texture texture;
    private Rectangle bounds;
    private Items.Item itemType;

    public DroppedItem(float x, float y, Items.Item itemType) {
        this.position = new Vector2(x, y);
        this.itemType = itemType;
        this.texture = new Texture(Gdx.files.internal("croptile/" + itemType.toString().toLowerCase() + "item.png"));
        this.bounds = new Rectangle(x, y, 16, 16);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
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
