package io.github.farmageddon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile {
    // Define fields, methods, and constructors for the Tile class
    private Texture texture;
    private int x, y;
    private Crop crop;
    private boolean isOccupied;
    public static final int TILE_WIDTH = 32;  // Example size
    public static final int TILE_HEIGHT = 32;
    public Tile(Texture texture, int x, int y, Crop crop) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.crop = crop;
        this.isOccupied = crop != null;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void dispose() {
        texture.dispose();
    }

    public void setCrop(Crop crop) {
        if (!isOccupied) {
            this.crop = crop;
            this.isOccupied = true;
            System.out.println("Crop " + crop.getType() + " planted on the tile.");
        } else {
            System.out.println("Tile is already occupied by another crop.");
        }
    }
}
