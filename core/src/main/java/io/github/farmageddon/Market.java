package io.github.farmageddon;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import io.github.farmageddon.markets.Item;

import java.util.ArrayList;
import java.util.List;

public class Market implements Screen {
    private final Main game;
    private Stage stage;
    private Table marketTable;
    private List<Item> items;
    private Skin skin;
    private boolean Visible;
    private TextButton buyButton;
    private TextButton sellButton;
    private Window marketWindow, inventoryWindow;


    public Market(Main game) {
        this.game = game;
        stage = new Stage(new StretchViewport(800, 600), game.batch);
        //table = new Table();

        // kich thuoc cua bang duoc dieu chinh phu hop voi kich thuoc cua man hinh
        //table.setFillParent(true);
        items = new ArrayList<>();
        //stage.addActor(table);
        populateMarket();
        Visible = true;

        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

    }


    // dien du lieu vao cho
    private void populateMarket() {
//        for (Item item : items) {
//            Label itemLabel = new Label(item.getName() + " - $" + item.getPrice(), skin);
//            TextButton buyButton = new TextButton("Buy", skin);
//            buyButton.addListener(event -> {
//                System.out.println("Bought: " + item.getName());
//                return true;
//            });
//
//            marketTable.add(itemLabel).pad(10);
//            marketTable.add(buyButton).pad(10);
//            marketTable.row();
//        }

        for (Item item : items) {
            addItemToMarket(item);
        }
    }

    private void addItemToMarket(Item item) {
        Label itemLabel = new Label(item.getName() + " - $" + item.getPrice(), skin);
        buyButton = new TextButton("Buy", skin);
        buyButton.addListener(event -> {
            System.out.println("Bought: " + item.getName());
            return true;
        });

        sellButton = new TextButton("Sell", skin);
        sellButton.addListener(event -> {
            System.out.println("Sold: " + item.getName());
            return true;
        });

        marketTable.add(itemLabel).pad(10);
        marketTable.add(buyButton).pad(10);
        marketTable.add(sellButton).pad(10);
        marketTable.row();
    }

    // kiem tra input M de bat tat market
    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            Visible = !Visible;
            marketWindow.setVisible(Visible);
            if (Visible){
                Gdx.app.log("MyTag", "Da chuyen sang market Screen!");
                Gdx.input.setInputProcessor(stage);
            }
            else {
                Gdx.input.setInputProcessor(null);
            }
        }
    }


    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        // khởi tạo cửa sổ Market
        marketWindow = new Window("Market", skin);
        marketWindow.setSize(800, 600);
        marketWindow.setPosition(0, 0);
        marketWindow.setVisible(Visible);


//        // Create inventory window
//        inventoryWindow = new Window("Inventory", skin);
//        inventoryWindow.setSize(400, 600);
//        inventoryWindow.setPosition(400, 0);
//        inventoryWindow.setVisible(false);

        // Create table for items in the shop
        marketTable = new Table();
        marketTable.top().center();
        marketTable.setFillParent(true);
        populateMarket();
        marketWindow.add(marketTable).fill().expand();
        stage.addActor(marketWindow);
        stage.addActor(inventoryWindow);


//        // tạo hình hộp chợ
//        Texture maket = new Texture(Gdx.files.internal("market_table.png"));
//        Image maketImage = new Image(maket);
//        Image invenImage = new Image(maket);
//        maketImage.setSize(maketImage.getWidth(), maketImage.getHeight());
//        maketImage.setPosition(0, 0);
//        stage.addActor(maketImage);
//
//        // tạo hình hộp inven
//        Texture inven = new Texture(Gdx.files.internal("Inventory.png"));
//        invenImage = new Image(inven);
//        invenImage.setSize(invenImage.getWidth(), invenImage.getHeight());
//        invenImage.setPosition(300, 300);
//        stage.addActor(invenImage);
//
//        skin = new Skin(Gdx.files.internal("uiskin.json"));
//
//        //tạo nút Buy
//        buyButton = new TextButton("Buy", skin);
//        buyButton.setPosition(200,200);
//        buyButton.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                System.out.println("Buyed");
//            }
//        });
//        stage.addActor(buyButton);
//
//        sellButton = new TextButton("Sell", skin);
//        sellButton.setPosition(100,100);
//        sellButton.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                System.out.println("Selled");
//            }
//        });
//        stage.addActor(sellButton);

    }

    @Override
    public void render(float delta) {
        handleInput();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));// cap nhat stage
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);// xoa man hinh trc khi hien len
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height,true );// Cập nhật viewport khi thay đổi kích thước
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
        stage.dispose(); // Giải phóng tài nguyên của stage
        skin.dispose(); // Giải phóng tài nguyên của skin

    }
}
