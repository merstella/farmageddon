package io.github.farmageddon;
// 1280 x 768
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.farmageddon.entities.Player;


public class InventoryUI {
    private final int titleSize;
    private final int slotCols = 5; // Số cốt
    private final int slotRows = 4;// số hàng
    public int slotCol; // vị trí con trỏ cột
    public int slotRow; // vị trí con trỏ hàng
    private Texture inventoryTexture;
    private Texture cursorTexture;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch = new SpriteBatch();

    // Equip inventory
    public InventoryUI(int titleSize) {
        this.titleSize = titleSize;
        this.inventoryTexture = new Texture(Gdx.files.internal("inventoryBar.png"));
        this.cursorTexture = new Texture(Gdx.files.internal("chickencutted/tile001.png"));
    }

    public void drawInventory(Player player) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float textureWidth = inventoryTexture.getWidth();
        float textureHeight = inventoryTexture.getHeight();

        // khung inventory
        spriteBatch.begin();
        spriteBatch.draw(inventoryTexture, (screenWidth - textureWidth)/2, 30,textureWidth*2,textureHeight*2);

        // vẽ các slot
        float slotSize = textureWidth / 5;
        float slotXstart = (screenWidth - textureWidth)/2;
        float slotYstart = 30;

        float slotX = slotXstart;
        float slotY = slotYstart;

        for (int i = 0; i < player.eqipInventory.size(); i++) {
            Texture itemTexture = player.eqipInventory.get(i).getTextureRegion().getTexture();
            spriteBatch.draw(itemTexture,slotX + 28,slotY + 16,slotSize * 1,slotSize * 1);
            slotX += (slotSize + 20);
        }

        //cursor
        float cursorX = slotXstart + 18 + ((titleSize + 15) * slotCol) ;
        float cursorY = slotYstart + 25;
        float cursorWeight = 0;
        float cursorHeight = 0;
        spriteBatch.draw(cursorTexture,cursorX,cursorY);

        spriteBatch.end();
    }
    public void disapose(){
        spriteBatch.dispose();
        inventoryTexture.dispose();
        shapeRenderer.dispose();
    }

}
