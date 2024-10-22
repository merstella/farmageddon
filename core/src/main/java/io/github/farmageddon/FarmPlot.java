package io.github.farmageddon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class FarmPlot {
    private int width, height;
    private Tile[][] tiles;
    private Crop crop;
    private Texture farmTexture;
    public FarmPlot(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        farmTexture = new Texture(Gdx.files.internal("farm.png"));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(farmTexture, x, y, crop); // Empty tile
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].render(batch);
            }
        }
    }

    public void plantCrop(int x, int y, Crop crop) {
        tiles[x][y].setCrop(crop);
    }
}
