package io.github.farmageddon;
// 1280 x 768
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.farmageddon.entities.Player;


public class InventoryUI {
    private final int titleSize;
    private BitmapFont fontNum;
    private Skin skin;
    private final int slotCols = 5; // Số cốt
    private final int slotRows = 4;// số hàng
    public int slotCol; // vị trí con trỏ cột
    public int slotRow; // vị trí con trỏ hàng
    private Texture inventoryTexture;
    private Texture selectedSheet;
    private TextureRegion cursorTexture;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch = new SpriteBatch();

    // Equip inventory
    public InventoryUI(int titleSize) {
        this.titleSize = titleSize;
        this.fontNum = new BitmapFont();
        this.skin = new Skin();
        fontNum.getData().setScale(1.5f);
        fontNum.setColor(Color.BLACK);
        skin.add("default", fontNum);
        this.inventoryTexture = new Texture(Gdx.files.internal("inventoryBar.png"));
        this.selectedSheet = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\UI_Selectors.png"));
        this.cursorTexture = new TextureRegion(selectedSheet, 3*(selectedSheet.getWidth()/4), 2*(selectedSheet.getHeight()/12), selectedSheet.getWidth()/4, selectedSheet.getHeight()/12);
    }

    public void drawInventory(Player player) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float textureWidth = inventoryTexture.getWidth();
        float textureHeight = inventoryTexture.getHeight();

        // khung inventory
        spriteBatch.begin();
        spriteBatch.draw(inventoryTexture, (screenWidth - textureWidth*3)/2, 30,textureWidth*3,textureHeight*3);

        // vẽ các slot
        float slotSize = (textureWidth * 3)/5 - 30;
        float slotXstart = (screenWidth - textureWidth*3)/2;
        float slotYstart = 35;

        float slotX = slotXstart;
        float slotY = slotYstart;

        for (int i = 0; i < player.eqipInventory.size(); i++) {
            Texture itemTexture = player.eqipInventory.get(i).getTextureRegion().getTexture();
            spriteBatch.draw(itemTexture,slotX + 32,slotY + 16,slotSize,slotSize);
            if(player.eqipInventory.get(i).getNum() > 1){
                fontNum.draw(spriteBatch,"x" + (player.eqipInventory.get(i).getNum()),slotX + 45,slotY + 25);
                System.out.println("x" + (player.eqipInventory.get(i).getNum()));
            }
            slotX += (slotSize + 20);
        }

        //cursor
        float cursorX = slotXstart + 5 + ((slotSize + 20) * slotCol)  ;
        float cursorY = slotYstart - 13;
        float cursorWeight = 0;
        float cursorHeight = 0;
        spriteBatch.draw(cursorTexture,cursorX,cursorY,cursorTexture.getRegionWidth()*2.3f,cursorTexture.getRegionHeight()*2.3f);
        spriteBatch.end();
    }
    public void disapose(){
        spriteBatch.dispose();
        inventoryTexture.dispose();
        shapeRenderer.dispose();
    }

}
