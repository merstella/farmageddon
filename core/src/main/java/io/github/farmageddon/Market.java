package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import io.github.farmageddon.Item;

import java.util.ArrayList;
import java.util.List;

public class Market implements ApplicationListener {
    private Stage stage;
    private Table table;
    private List<Item> items;
    private Skin skin;
    private boolean Visible;
    private TextButton buyButton;
    private TextButton sellButton;
    private Window marketWindow;

    private Image maketImage;
    private Image invenImage;
    private SpriteBatch batch;

    public Market(Skin skin) {
        this.skin = skin;
        stage = new Stage();
        table = new Table();
        // kich thuoc cua bang duoc dieu chinh phu hop voi kich thuoc cua man hinh
        table.setFillParent(true);
        items = new ArrayList<>();
        stage.addActor(table);
        populateMarket();
        Visible = false;
    }

    // dien du lieu vao cho
    private void populateMarket() {
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

        table.add(itemLabel).pad(10);
        table.add(buyButton).pad(10);
        table.add(sellButton).pad(10);
        table.row();
    }

    // kiem tra input M de bat tat market
    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            Visible = !Visible;
            if (Visible){
                Gdx.input.setInputProcessor(stage);
            }
            else {
                Gdx.input.setInputProcessor(null);
            }
        }
    }

    // ve market ra nhe' !!!


    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage(new StretchViewport(800, 600), batch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // khởi tạo cửa sổ Market
        marketWindow = new Window("Maket", skin);
        marketWindow.setSize(800,600);
        marketWindow.setPosition(0,0);
        marketWindow.setVisible(Visible);

        table = new Table();
        table.setFillParent(true);
        populateMarket();// thêm vật phẩm vào cửa sổ

        marketWindow.addActor(table);
        stage.addActor(marketWindow);



// phần này đang lỗi
//        // tạo hình hộp chợ
//        Texture maket = new Texture(Gdx.files.internal("market_table.png"));
//        maketImage = new Image(maket);
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
    public void resize(int width, int height) {
        stage.getViewport().update(width, height,true );// Cập nhật viewport khi thay đổi kích thước
    }

    @Override
    public void render() {
        handleInput();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));// cap nhat stage
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);// xoa man hinh trc khi hien len
        stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose(); // Giải phóng tài nguyên của stage
        skin.dispose(); // Giải phóng tài nguyên của skin
        batch.dispose(); // Giải phóng tài nguyên của SpriteBatch
    }
}
