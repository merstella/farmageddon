package io.github.farmageddon;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.farmageddon.Crops.Crop;
import io.github.farmageddon.Crops.Land;
import io.github.farmageddon.entities.Monster;
import io.github.farmageddon.entities.Player;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.DroppedItem;
import io.github.farmageddon.ultilites.Items;

import java.util.ArrayList;
import java.util.Objects;
//import static io.github.farmageddon.ultilites.CollisionHandling.gameScreen;

public class InventoryScreen implements Screen, InputProcessor {
    private int titleSize;
    public int slotRow = 0;
    public int slotCol = 0;
    private Texture inventoryTexture;
    private Texture testTexture;
    public SpriteBatch batch;
    public Stage stage;
    public final Vector3[] touchPosition = new Vector3[25];
    private InventoryUI inventoryUI;
    public Player player;
    private GameScreen game;
    public Texture selectorsSheet;
    public TextureRegion cursorTexture;
    private Texture UI_sheet;
    private TextureRegion ButtonActive;
    private TextureRegion ButtonInactive;
    private Skin skin;
    private Items none1;
    private Items none2;

    // Player Inventory
    public InventoryScreen(int titleSize, Player player) {
        this.titleSize = titleSize;
        this.inventoryTexture = new Texture(Gdx.files.internal("inventoryBox.png"));
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        this.player = player;
        this.UI_sheet = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\UI_Buttons.png"));
        this.selectorsSheet = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\UI_Selectors.png"));
        this.cursorTexture = new TextureRegion(selectorsSheet, 3*(selectorsSheet.getWidth()/4), 2*(selectorsSheet.getHeight()/12), selectorsSheet.getWidth()/4, selectorsSheet.getHeight()/12);
        this.testTexture = new Texture(Gdx.files.internal("box.png"));
        this.skin = new Skin();
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.8f);
        skin.add("default", font);

        ButtonActive = new TextureRegion(UI_sheet,0,(UI_sheet.getHeight() / 19),32 ,14);
        ButtonInactive = new TextureRegion(UI_sheet,33, (UI_sheet.getHeight() / 19),32 ,14);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = new TextureRegionDrawable(ButtonActive); // Trạng thái bình thường
        buttonStyle.down = new TextureRegionDrawable(ButtonInactive); // Trạng thái nhấn
        buttonStyle.fontColor = Color.BLACK;
        skin.add("default", buttonStyle);
        none1 = GameScreen.Default;
        none2 = GameScreen.Default;
        createButtons(buttonStyle);
    }

    private void createButtons(TextButton.TextButtonStyle buttonStyle) {
        // Tạo nút Play
        TextButton swapButton = new TextButton("SWAP", buttonStyle);
        swapButton.getLabel().setAlignment(Align.center); // Căn giữa chữ
        swapButton.setSize(125, 60);
        swapButton.setPosition((Gdx.graphics.getWidth() - 125)/4 * 3, (Gdx.graphics.getHeight() - 60) / 2);
        swapButton.addListener(event -> {
            if (swapButton.isPressed()) {
                none1 = player.inventory.get(player.inventoryCursor);
                none2 = player.eqipInventory.get(player.slotCursor);
                player.setItem(none2,player.inventoryCursor);
                player.setEquipItem(none1,player.slotCursor);
                none1 = GameScreen.Default;
                none2 = GameScreen.Default;
            }
            return true;
        });
        stage.addActor(swapButton);
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
        // cursor
        float cursorX = slotXstart + (slotSize * slotCol);
        float cursorY = slotYstart - (slotSize * slotRow);

        batch.draw(inventoryTexture,(screenWidth-textureWidth*2)/2,(screenHeight-textureHeight*2)/2, textureWidth*2, textureHeight*2);
        for (int i = 0; i < player.inventory.size(); i++) {
            Texture itemTexture = player.inventory.get(i).getTextureRegion().getTexture();
            batch.draw(itemTexture, slotX, slotY, itemTexture.getWidth()*2, itemTexture.getHeight()*2);
            slotX += slotSize;
            if (i == 4 || i == 9 || i== 14 ||i == 19){
                slotX = slotXstart;
                slotY -= slotSize;
            }
        }
        slotX = slotXstart;
        slotY = slotYstart;

        // các slot gán tọa độ
        for (int i = 0; i < 25; i++) {
            touchPosition[i] = new Vector3(slotX,slotY,0);
            System.out.println(touchPosition[i]);
            if ((i + 1) % 5 == 0) {
                slotX = slotXstart;
                slotY -= slotSize;
            }
            else {
                slotX += slotSize;
            }
        }

        batch.draw(cursorTexture, cursorX - 20, cursorY - 20,cursorTexture.getRegionWidth() * 1.5f, cursorTexture.getRegionHeight() * 1.5f );
        int slotColUnreal = 0;
        int slotRowUnreal = 0;
        for (int i = 0; i < player.maxInventorySize ; i++){
            if( i == player.inventoryCursor){
                break;
            }
            if(slotColUnreal < 4){
                slotColUnreal++;
            }
            else {
                slotColUnreal = 0;
                slotRowUnreal++;
            }
            if (slotRowUnreal >= 5) {
                break;
            }
            if (slotColUnreal >= 5){
                break;
            }
        }
        slotRow = slotRowUnreal;
        slotCol = slotColUnreal;
        stage.act(v);
        stage.draw();
        batch.end();
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
        if (inventoryTexture != null) inventoryTexture.dispose();
        if (batch != null) batch.dispose();
        stage.dispose();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) { // Chỉ xử lý nếu nhấn nút trái chuột
            Vector3 touch = new Vector3(screenX, screenY, 0);
            touch = stage.getCamera().unproject(touch);
            // Duyệt qua tất cả các slot
            for (int i = 0; i < 25; i++) {
                float slotSize = (inventoryTexture.getWidth() * 2) / 5 - 12;
                Vector3 slot = touchPosition[i]; // Lấy tọa độ của slot
                if (touch.x >= slot.x && touch.x <= slot.x + slotSize && touch.y >= slot.y && touch.y <= slot.y + slotSize) {
                    handleSlotTouch(i);
                    return true;
                }
            }
        }
        return false;
    }

    private void handleSlotTouch(int slotIndex) {
        player.inventoryCursor = slotIndex;
        System.out.println("cursor = " + player.inventoryCursor);
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
