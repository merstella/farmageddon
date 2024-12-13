package io.github.farmageddon;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.farmageddon.entities.Player;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.Items;

public final class MarketScreen implements Screen, InputProcessor {

    private final int titleSize;
    private int slotColInventory = 0;
    private int slotRowInventory = 0;
    private int slotColMarket = 0;
    private int slotRowMarket = 0;
    private Texture marketTexture;
    private SpriteBatch batch;
    public Texture selectorsSheet;
    private TextureRegion cursorTexture;
    private Market market;
    private Texture ui_sheet;
    private TextureRegion TextureInactive;
    private TextureRegion TextureActive;
    private Texture moneyBar;
    public Player player;
    private Skin skin;
    public Stage stage;
    public final Vector3[] touchInvenPosition = new Vector3[25];
    public final Vector3[] touchMarketPosition = new Vector3[25];
    private BitmapFont font;
    public Game game;
    public GameScreen gameScreen;
    public Main main;


    public MarketScreen(int titleSize, Market market, Player player) {
        this.player = player;
        this.gameScreen = gameScreen;
        this.titleSize = titleSize;
        this.marketTexture = new Texture(Gdx.files.internal("inventoryBox.png"));
        this.market = market;
        this.batch = new SpriteBatch();
        this.selectorsSheet = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\UI_Selectors.png"));
        this.cursorTexture = new TextureRegion(selectorsSheet, 3*(selectorsSheet.getWidth()/4), 2*(selectorsSheet.getHeight()/12), selectorsSheet.getWidth()/4, selectorsSheet.getHeight()/12);
        this.skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        this.moneyBar = new Texture(Gdx.files.internal("moneyBar.png"));
        this.font = new BitmapFont();
        this.ui_sheet = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\UI_Buttons.png"));
        this.TextureActive = new TextureRegion(ui_sheet,0,(ui_sheet.getHeight() / 19),32 ,14);
        this.TextureInactive = new TextureRegion(ui_sheet,33, (ui_sheet.getHeight() / 19),32 ,14);
        font.getData().setScale(1.8f);
        skin.add("default", font);

        stage = new Stage(new ScreenViewport());

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = new TextureRegionDrawable(TextureActive);// Trạng thái bình thường
        buttonStyle.down = new TextureRegionDrawable(TextureInactive); // Trạng thái nhấn
        buttonStyle.fontColor = Color.BLACK;
        skin.add("default", buttonStyle);

        createButtons(buttonStyle);

    }

    private void createButtons(TextButton.TextButtonStyle buttonStyle) {
        // Tạo nút Buy
        TextButton buyButton = new TextButton("BUY", buttonStyle);
        buyButton.getLabel().setAlignment(Align.center); // Căn giữa chữ
        buyButton.pad(20); // Thêm khoảng cách xung quanh chữ
        buyButton.setSize(175, 60);
        buyButton.setPosition(2*(Gdx.graphics.getWidth()-175)/3,(Gdx.graphics.getHeight()-60)/4 );
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Items selectedItem = market.marketItems.get(market.marketCursor);
                if (player.money >= selectedItem.getCost()) {
                    for (int i = 0; i < player.maxInventorySize; i++) {
                        if (player.inventory.get(i).getItem() == Items.Item.DEFAULT) {
                            player.setItem(selectedItem, i);
                            player.subMoney(selectedItem.getCost()); // Giảm tiền
                            break;
                        }
                    }
                } else {
                    System.out.println("Not enough money!");
                }
            }
        });

        // Tạo nút Exit
        TextButton sellButton = new TextButton("SELL", buttonStyle);
        sellButton.setSize(175, 60);
        sellButton.getLabel().setAlignment(Align.center); // Căn giữa chữ
        sellButton.pad(20); // Thêm khoảng cách xung quanh chữ
        sellButton.setPosition((Gdx.graphics.getWidth()-175)/3 , (Gdx.graphics.getHeight()-60)/4);
        sellButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Sell Clicked");
                Items selectedItem = player.inventory.get(player.inventoryCursor);
                player.addMoney(selectedItem.getCost());
                player.removeItem(selectedItem);
            }
        });

        // Thêm nút vào Stage
        stage.addActor(buyButton);
        stage.addActor(sellButton);
        Gdx.input.setInputProcessor(stage);
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
        float textureWidth = marketTexture.getWidth();
        float textureHeight = marketTexture.getHeight();
        // moneyBar
        batch.draw(moneyBar,200,screenHeight - 100,moneyBar.getWidth()*3,moneyBar.getHeight()*3);
        font.draw(batch,String.valueOf(player.money), 220, screenHeight - 62);
        batch.draw(moneyBar, screenWidth/4, screenHeight/4 + 100,moneyBar.getWidth()*3,moneyBar.getHeight()*3);
        batch.draw(moneyBar, 3*(screenWidth/4), screenHeight/4 + 100,moneyBar.getWidth()*3,moneyBar.getHeight()*3);

        // draw 2 inventoryBox
        batch.draw(marketTexture,3*(screenWidth-textureWidth*2)/4,3*(screenHeight-textureHeight*2)/4, textureWidth*2, textureHeight*2);
        batch.draw(marketTexture,(screenWidth-textureWidth*2)/4,3*(screenHeight-textureHeight*2)/4, textureWidth*2, textureHeight*2);

        // slot Inventory
        float slotSize = (textureWidth * 2)/ 5 - 12  ;
        float SizeOfSlot= textureWidth / 5;
        float slotXstartInventory = ((screenWidth-textureWidth*2)/4) + 39 ;
        float slotYstartInventory = (3*(screenHeight-textureHeight*2)/4) + 204;
        float slotXInventory = slotXstartInventory;
        float slotYInventory = slotYstartInventory;

        for (int i = 0; i < player.inventory.size(); i++) {
            Texture itemTexture = player.inventory.get(i).getTextureRegion().getTexture();
            batch.draw(itemTexture, slotXInventory, slotYInventory, SizeOfSlot, SizeOfSlot);
            slotXInventory += slotSize;
            if ((i == 4 || i == 9 || i== 14 || i == 19) && i < 26 ){
                slotXInventory = slotXstartInventory;
                slotYInventory -= slotSize;
            }
        }

        // slot market
        float slotXstartMarket = (3*(screenWidth-textureWidth*2)/4) + 39 ;
        float slotYstartMarket = (3*(screenHeight-textureHeight*2)/4) + 204;
        float slotXMarket = slotXstartMarket;
        float slotYMarket = slotYstartMarket;
        for (int i = 0; i < market.marketItems.size(); i++) {
            Texture itemTexture = market.marketItems.get(i).getTextureRegion().getTexture();
            batch.draw(itemTexture, slotXMarket, slotYMarket, SizeOfSlot, SizeOfSlot);
            slotXMarket += slotSize;
            if ((i == 4 || i == 9 || i== 14 || i == 19) && i < 26 ){
                slotXMarket = slotXstartMarket;
                slotYMarket -= slotSize;
            }
        }

        // cursor market
        float cursorXMarket = slotXstartMarket + (slotSize * slotColMarket);
        float cursorYMarket = slotYstartMarket - (slotSize * slotRowMarket);
        batch.draw(cursorTexture, cursorXMarket - 20, cursorYMarket - 20,cursorTexture.getRegionWidth()*1.5f,cursorTexture.getRegionHeight()*1.5f);
        // cursor Inventory
        float cursorXInventory = slotXstartInventory + (slotSize * slotColInventory);
        float cursorYInventory = slotYstartInventory - (slotSize * slotRowInventory);
        batch.draw(cursorTexture, cursorXInventory - 20, cursorYInventory - 20,cursorTexture.getRegionWidth()*1.5f,cursorTexture.getRegionHeight()*1.5f);

        int slotColInventoryUnreal = 0;
        int slotRowInventoryUnreal = 0;
        for (int i = 0; i < player.maxInventorySize ; i++){
            if( i == player.inventoryCursor){
                break;
            }
            if(slotColInventoryUnreal < 4){
                slotColInventoryUnreal++;
            }
            else {
                slotColInventoryUnreal = 0;
                slotRowInventoryUnreal++;
            }
            if (slotRowInventoryUnreal >= 5) {
                break;
            }
            if (slotColInventoryUnreal >= 5){
                break;
            }
        }
        slotRowInventory = slotRowInventoryUnreal;
        slotColInventory = slotColInventoryUnreal;

        int slotColMarketUnreal = 0;
        int slotRowMarketUnreal = 0;
        for (int i = 0; i < market.maxMarketItems ; i++){
            if( i == market.marketCursor){
                break;
            }
            if(slotColMarketUnreal < 4){
                slotColMarketUnreal++;
            }
            else {
                slotColMarketUnreal = 0;
                slotRowMarketUnreal++;
            }
            if (slotRowMarketUnreal >= 5) {
                break;
            }
            if (slotColMarketUnreal >= 5){
                break;
            }
        }
        slotRowMarket = slotRowMarketUnreal;
        slotColMarket = slotColMarketUnreal;

        // các slot gán tọa độ Inven
        slotXInventory = slotXstartInventory;
        slotYInventory = slotYstartInventory;
        for (int i = 0; i < 25; i++) {
            touchInvenPosition[i] = new Vector3(slotXInventory,slotYInventory,0);
//            System.out.println(touchInvenPosition[i]);
            if ((i + 1) % 5 == 0) {
                slotXInventory = slotXstartInventory;
                slotYInventory -= slotSize;
            }
            else {
                slotXInventory += slotSize;
            }
        }

        // gán tọa độ market
        slotXMarket = slotXstartMarket;
        slotYMarket = slotYstartMarket;
        for (int i = 0; i < 25; i++) {
            touchMarketPosition[i] = new Vector3(slotXMarket,slotYMarket,0);
//            System.out.println(touchMarketPosition[i]);
            if ((i + 1) % 5 == 0) {
                slotXMarket = slotXstartMarket;
                slotYMarket -= slotSize;
            }
            else {
                slotXMarket += slotSize;
            }
        }

        // draw cost to sell
        float costXInventory = screenWidth/4 + 30;
        float costYInventory = screenHeight/4 + 136;
        int itemIndexInventory = getItemIndexOnInventory();
        if (itemIndexInventory < player.inventory.size()){
            font.draw(batch, String.valueOf(player.inventory.get(itemIndexInventory).getCost()), costXInventory, costYInventory);
        }
        float costXmarket = 3*screenWidth/4 + 30;
        float costYmarket = screenHeight/4 + 136;
        int itemIndexMarket = getItemIndexOnInventory();
        if (itemIndexMarket < market.marketItems.size()){
            font.draw(batch,String.valueOf(market.marketItems.get(itemIndexMarket).getCost()),costXmarket,costYmarket);
        }

        stage.act(v); // Cập nhật stage
        stage.draw();
        batch.end();
        System.out.println(player.inventory.get(player.inventoryCursor).getItem());
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

    // lấy itemIndex
    public int getItemIndexOnInventory(){
        int itemIndexInventory = slotColInventory + (slotRowInventory*5);
        return itemIndexInventory;
    }
    public int getItemIndexMarket(){
        int itemIndexMarket = slotColMarket + (slotRowMarket*5);
        return itemIndexMarket;
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
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
                float slotSize = (marketTexture.getWidth() * 2)/ 5 - 12  ;
                Vector3 slot = touchInvenPosition[i]; // Lấy tọa độ của slot
                Vector3 slot1 = touchMarketPosition[i]; // Lấy tọa độ của slot
                if (touch.x >= slot.x && touch.x <= slot.x + slotSize && touch.y >= slot.y && touch.y <= slot.y + slotSize) {
                    handleSlotInvenTouch(i);
                    return true;
                }
                if(touch.x >= slot1.x && touch.x <= slot1.x + slotSize && touch.y >= slot1.y && touch.y <= slot1.y + slotSize){
                    handleSlotMarketTouch(i);
                    return true;
                }
            }
        }
        return false;
    }

    private void handleSlotInvenTouch(int slotIndex) {
        player.inventoryCursor = slotIndex;
        System.out.println("cursor = " + player.inventoryCursor);
    }

    private void handleSlotMarketTouch(int slotIndex) {
        market.marketCursor = slotIndex;
        System.out.println("cursor = " + market.marketCursor);
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
