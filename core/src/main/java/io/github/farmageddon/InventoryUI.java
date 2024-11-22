package io.github.farmageddon.markets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class InventoryUI {
    private final int titleSize;
    private final int slotCols = 5; // Số cốt
    private final int slotRows = 4;// số hàng
    private final int slotSize;
    private int slotCol; // vị trí con trỏ cột
    private int slotRow; // vị trí con trỏ hàng

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch = new SpriteBatch();

    public InventoryUI(int titleSize) {
        this.titleSize = titleSize;
        this.slotSize = titleSize;
    }

    public void drawInventory

}
