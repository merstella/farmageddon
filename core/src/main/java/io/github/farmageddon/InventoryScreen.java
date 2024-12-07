package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.farmageddon.entities.Player;
import io.github.farmageddon.screens.GameScreen;

public class InventoryScreen implements Screen {
    private final int titleSize;
    public int slotRow = 0;
    public int slotCol = 0;
    private Texture inventoryTexture;
    private Texture equipmentTexture;
    public SpriteBatch batch;
//    private InventoryUI inventoryUI;
    public Player player;
    private GameScreen game;
    public Texture cursorTexture;

    // Player Inventory
    public InventoryScreen(int titleSize, Player player) {
        this.titleSize = titleSize;
        this.inventoryTexture = new Texture(Gdx.files.internal("inventoryBox.png"));
        this.batch = new SpriteBatch();
        this.player = player;
        this.cursorTexture = new Texture(Gdx.files.internal("chickencutted/tile001.png"));
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

        batch.begin();

        // kich thuoc cua screen
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // kich thuoc cua texture
        float textureWidth = inventoryTexture.getWidth();
        float textureHeight = inventoryTexture.getHeight();

        // slot
        float slotSize = (textureWidth * 2)/ 5 - 12  ;
        float slotXstart = ((screenWidth-textureWidth*2)/2) + 39 ;
        float slotYstart = ((screenHeight-textureHeight*2)/2) + 204;
        float slotX = slotXstart;
        float slotY = slotYstart;

        batch.draw(inventoryTexture,(screenWidth-textureWidth*2)/2,(screenHeight-textureHeight*2)/2, textureWidth*2, textureHeight*2);

        System.out.println(player.inventory.size());
        for (int i = 0; i < player.inventory.size(); i++) {
            Texture itemTexture = player.inventory.get(i).getTextureRegion().getTexture();
            batch.draw(itemTexture, slotX, slotY, itemTexture.getWidth(), itemTexture.getHeight());
            slotX += slotSize;
            if (i == 4 || i == 9 || i== 14){
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // cursor
        float cursorX = slotXstart + (slotSize * slotCol);
        float cursorY = slotYstart - (slotSize * slotRow);
        float cursorWidth = slotSize;
        float cursorHeight = slotSize;
        batch.draw(cursorTexture, cursorX, cursorY);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            if (slotRow != 0){
                slotRow--;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            if (slotRow != 4){
                slotRow++;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            if (slotCol != 4){
                slotCol++;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            if(slotCol != 0){
                slotCol--;
            }
        }



        batch.end();

        //inventoryUI.drawInventory(player);
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
//        inventoryTexture.dispose();
//        batch.dispose();
        if (inventoryTexture != null) inventoryTexture.dispose();
        if (batch != null) batch.dispose();
    }
}
