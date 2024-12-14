package io.github.farmageddon.Crops;

import com.badlogic.gdx.math.Rectangle;
import io.github.farmageddon.screens.GameScreen;

public class LandManager {
    private Land[][] landGrid;
    private int maxY;
    private int maxX;

    public LandManager(int maxY, int maxX) {
        this.maxY = maxY;
        this.maxX = maxX;
        landGrid = new Land[maxY][maxX];
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                landGrid[i][j] = new Land();
            }
        }
    }

    public void update(float delta) {
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                landGrid[i][j].update(delta);
            }
        }
    }

    public Land getLand(int y, int x) {
        return landGrid[y][x];
    }
}
