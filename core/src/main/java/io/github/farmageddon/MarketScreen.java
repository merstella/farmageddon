package io.github.farmageddon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.farmageddon.entities.Player;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.Items;

public class MarketScreen implements Screen {
    private final int titleSize;
    private int slotColInventory = 0;
    private int slotRowInventory = 0;
    private int slotColMarket = 0;
    private int slotRowMarket = 0;
    private Texture marketTexture;
    private SpriteBatch batch;
    private Texture cursorTexture;
    private Market market;
    private TextButton buyButton;
    private TextButton sellButton;
    private Texture buyTexture;
    private Texture sellTexture;
    private Texture moneyBar;
    public Player player;
    private Skin skin;
    private Stage stage;
    private BitmapFont font;
    public Game game;
    public GameScreen gameScreen;
    public Main main;

    public MarketScreen(int titleSize, Market market, Player player) {
        this.player = player;
        this.game = game;
        this.main = main;
        this.gameScreen = gameScreen;
        this.titleSize = titleSize;
        this.marketTexture = new Texture(Gdx.files.internal("inventoryBox.png"));
        this.market = market;
        this.batch = new SpriteBatch();
        this.cursorTexture = new Texture(Gdx.files.internal("chickencutted/tile001.png"));
        this.skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        this.moneyBar = new Texture(Gdx.files.internal("moneyBar.png"));

        this.buyTexture = new Texture(Gdx.files.internal("BuyButton.png"));
        this.sellTexture = new Texture(Gdx.files.internal("SellButton.png"));

        stage = new Stage(new ScreenViewport());

        TextureRegionDrawable buyDrawable = new TextureRegionDrawable(buyTexture);
        ImageButton buyButton = new ImageButton(buyDrawable);
        buyButton.setSize(titleSize, titleSize);
        buyButton.setPosition(3*(Gdx.graphics.getWidth() / 4), Gdx.graphics.getHeight() / 4);

        TextureRegionDrawable sellDrawable = new TextureRegionDrawable(sellTexture);
        ImageButton sellButton = new ImageButton(sellDrawable);
        sellButton.setSize(titleSize, titleSize);
        sellButton.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);

        // khởi tạo font
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        // su kien bam nut
        sellButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Sell Clicked");
                int itemIndexinventory = getItemIndexOnInventory();//
                if (itemIndexinventory < player.inventory.size()) {
                    Items selectedItem = player.inventory.get(itemIndexinventory);
                    player.addMoney(selectedItem.getCost());
                    player.inventory.remove(itemIndexinventory);

                }
            }
        });

        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("buy Clicked");
                int itemIndexInventory = getItemIndexOnInventory(); // Lấy chỉ mục mục được chọn
                if (itemIndexInventory < market.marketItems.size()) {
                    Items selectedItem = market.marketItems.get(itemIndexInventory);
                    if (player.money >= selectedItem.getCost()) {
                        player.subMoney(selectedItem.getCost()); // Giảm tiền
                        market.marketItems.remove(itemIndexInventory); // Loại bỏ mục
                        player.inventory.add(selectedItem);// thêm item vào inventory
                        System.out.println("Item purchaed: " + selectedItem.getItem());
                        System.out.println("Remaining money: " + player.money);
                    } else {
                        System.out.println("Not enough money!");
                    }
                } else {
                    System.out.println("No item selected!");
                }

            }
        });


        stage.addActor(buyButton);
        stage.addActor(sellButton);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        float slotXstartInventory = ((screenWidth-textureWidth*2)/4) + 39 ;
        float slotYstartInventory = (3*(screenHeight-textureHeight*2)/4) + 204;
        float slotXInventory = slotXstartInventory;
        float slotYInventory = slotYstartInventory;

        for (int i = 0; i < player.inventory.size(); i++) {
            Texture itemTexture = player.inventory.get(i).getTextureRegion().getTexture();
            batch.draw(itemTexture, slotXInventory, slotYInventory, itemTexture.getWidth(), itemTexture.getHeight());
            slotXInventory += slotSize;
            if (i == 4 || i == 9 || i== 14){
                slotXInventory = slotXstartInventory;
                slotYInventory += slotSize;
            }
        }

        // slot market
        float slotXstartMarket = (3*(screenWidth-textureWidth*2)/4) + 39 ;
        float slotYstartMarket = (3*(screenHeight-textureHeight*2)/4) + 204;
        float slotXMarket = slotXstartMarket;
        float slotYMarket = slotYstartMarket;

        //System.out.println(market.marketItems.size());
        for (int i = 0; i < market.marketItems.size(); i++) {
            Texture itemTexture = market.marketItems.get(i).getTextureRegion().getTexture();
            batch.draw(itemTexture, slotXMarket, slotYMarket, itemTexture.getWidth(), itemTexture.getHeight());
            slotXMarket += slotSize;
            if (i == 4 || i == 9 || i== 14){
                slotXMarket = slotXstartInventory;
                slotYMarket += slotSize;
            }
        }

        // cursor market
        float cursorXMarket = slotXstartMarket + (slotSize * slotColMarket);
        float cursorYMarket = slotYstartMarket - (slotSize * slotRowMarket);
        float cursorWidth = slotSize;
        float cursorHeight = slotSize;
        batch.draw(cursorTexture, cursorXMarket, cursorYMarket);
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            if (slotRowMarket != 0){
                slotRowMarket--;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            if (slotRowMarket != 4){
                slotRowMarket++;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)){
            if (slotColMarket != 4){
                slotColMarket++;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)){
            if(slotColMarket != 0){
                slotColMarket--;
            }
        }

        // draw cost
        //System.out.println(slotRowInventory);
        float costXInventory = screenWidth/4 + 30;
        float costYInventory = screenHeight/4 + 136;
        int itemIndexInventory = getItemIndexOnInventory();
        if (itemIndexInventory < player.inventory.size()){
            font.draw(batch, String.valueOf(player.inventory.get(itemIndexInventory).getCost()), costXInventory, costYInventory);
        }


        // cursor Inventory
        float cursorXInventory = slotXstartInventory + (slotSize * slotColInventory);
        float cursorYInventory = slotYstartInventory - (slotSize * slotRowInventory);
//        float cursorWidth = slotSize;
//        float cursorHeight = slotSize;
        batch.draw(cursorTexture, cursorXInventory, cursorYInventory);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            if (slotRowInventory != 0){
                slotRowInventory--;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            if (slotRowInventory != 4){
                slotRowInventory++;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            if (slotColInventory != 4){
                slotColInventory++;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            if(slotColInventory != 0){
                slotColInventory--;
            }
        }

        stage.act(v); // Cập nhật stage
        stage.draw();

//        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
//            System.out.println("GameScreen");
//            game.setScreen(new GameScreen(main));
//        }


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

    // lấy itemIndex
    public int getItemIndexOnInventory(){
        int itemIndexInventory = slotColInventory + (slotRowInventory*5);
        return itemIndexInventory;
    }



    // sell Action


    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
