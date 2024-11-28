package io.github.farmageddon.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.farmageddon.*;

import io.github.farmageddon.markets.Items;
import io.github.farmageddon.markets.Market;
import io.github.farmageddon.ultilites.GameTimeClock;
import io.github.farmageddon.ultilites.Timer_;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.badlogic.gdx.graphics.Color.WHITE;

public class GameScreen implements Screen, InputProcessor {
    private final Main game;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Player player; // Your player class
    public static TiledMap map; // Your game map
    private OrthogonalTiledMapRenderer mapRenderer;
    private Vector3 touchPosition = new Vector3();
    private BitmapFont font;
    public static ShapeRenderer shapeRenderer;

    private GameTimeClock clock;
    private Timer_ timer;
    private boolean showDebugInfo = true;
    private String time;
    private static Label timeLabel, timeStringLabel, daysLeftLabel, daysLeftNum;
    private static Label scoreLabel;
    private static Label scoreStringLabel;
    private static int daysLeft;

    private Texture equipmentTexture;
    private InventoryUI inventoryUI;
    public Market market;
    private InventoryScreen inventoryScreen;
    private MarketScreen marketScreen;
    private boolean isMarketVisible = false;
    private boolean isInventoryVisible = false;
    public static boolean isFishingVisible = false;

    // Items Texture
    public Texture CoinTexture;
    public Texture item1Texture;
    public Texture item2Texture;
    public Texture item3Texture;
    public Texture item4Texture;
    public Texture FishTexture;
    public Items items;
    public static Items Fish;

    //fishing minigame
    public FishingMinigame minigame;

    private Music music;

    private Animal animal;
    private Stage stage;

    private Vector2 selectedCell;
    int currentDays;
    public GameScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Main.GAME_WIDTH, Main.GAME_HEIGHT, camera);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        camera.zoom = 0.25f;
        camera.update();
        map = new TmxMapLoader().load("mapok.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        stage = new Stage(viewport);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(WHITE);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        selectedCell = new Vector2();

        timer = new Timer_();
        timer.setTimeRatio(9000);
        timer.StartNew(120, true, true);
        timer.setStartTime(0, 12, 30, 0);
        clock = new GameTimeClock(timer);
        time = timer.getFormattedTimeofDay();
        currentDays = 0;
        daysLeft = 30;

        player = new Player(640, 300, 100f);
        market = new Market(100, 100, 200);
        initMarket();
        animal = new Animal(680, 230, "Chicken", stage);
        animal.setBound(620, 690, 160, 260);

        initDebug();
        music = Main.manager.get("Sound/music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
    }
    public void initMarket() {

        // Test items inventory System
        FishTexture = new Texture(Gdx.files.internal("Animals\\Bee\\Bee_Hive.png"));
        Fish = new Items("Fish", FishTexture, 10);

        CoinTexture = new Texture(Gdx.files.internal("Coin_Icon.png"));
        items = new Items("Coin", CoinTexture, 10);
        player.setEquipItem(items);// them vao equip

        item1Texture = new Texture(Gdx.files.internal("SellButton.png"));
        items = new Items("Sell", item1Texture, 10);
        player.setEquipItem(items);

        item2Texture = new Texture(Gdx.files.internal("BuyButton.png"));
        items = new Items("Buy", item2Texture, 10);
        player.setEquipItem(items);// them vao inventory

        item3Texture = new Texture(Gdx.files.internal("moneyBar.png"));
        items = new Items("Money", item3Texture, 10);
        player.setEquipItem(items);

        item4Texture = new Texture(Gdx.files.internal("Well.png"));
        items = new Items("Well", item4Texture, 10);
        player.setItem(items);

        //money
        player.addMoney(200);
    }
    public void initDebug() {
        timeLabel = new Label(time, new Label.LabelStyle(font, WHITE));
        timeStringLabel = new Label("Time", new Label.LabelStyle(font, WHITE));
        timeStringLabel.setFontScale(2);
        timeLabel.setFontScale(2);
        daysLeftLabel = new Label("Days Left", new Label.LabelStyle(font, WHITE));
        daysLeftNum = new Label(String.format("%d", daysLeft), new Label.LabelStyle(font, WHITE));
        daysLeftLabel.setFontScale(2);
        daysLeftNum.setFontScale(2);
    }
    @Override
    public void show() {
        // inventory & market
        int titleSize = 32;
        inventoryUI = new InventoryUI(titleSize);
        marketScreen = new MarketScreen(titleSize,market, player);
        inventoryScreen = new InventoryScreen(titleSize, player);

        // fishing minigame
        // fishing minigame
        minigame = new FishingMinigame();
        minigame.create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        player.update(delta);
        camera.position.set(
            player.getPosition().x + 16,
            player.getPosition().y + 16,
            0
        );
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();
        if(timer.getDaysPassed() != currentDays){
            currentDays = timer.getDaysPassed();
            if (currentDays % 2 == 0) animal.incState();
            daysLeft--;
            daysLeftNum.setText(String.format("%d", daysLeft));
        }

        clock.act(delta);
        animal.update(delta, currentDays);
        stage.act();
        stage.draw();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            animal.feed();
        }
        //printAnimalDebugInfo();
        time = timer.getFormattedTimeofDay();
        timeLabel.setText(time);
        shapeRenderer.setProjectionMatrix(camera.combined);
        renderPlayer();
        renderSelectedCell();
        renderAmbientLighting();
        renderDebugInfo();
        CollisionHandling.renderCollision();

        handleKeyDown(delta);
    }

    private void handleKeyDown(float delta) {
        // bấm M để hiện lên cửa sổ market
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            isMarketVisible = !isMarketVisible;
            System.out.println("marketScreen");

            if (isMarketVisible == true) {
                marketScreen.show();
            } else {
                marketScreen.hide();
            }
            //game.setScreen(marketScreen);
        }
        if (isMarketVisible) {
            marketScreen.render(delta);
        }

        // bấm B để hiện lên cửa sổ inventory
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)){
            isInventoryVisible = !isInventoryVisible;
            System.out.println("inventoryScreen");

            if (isInventoryVisible == true) {
                inventoryScreen.show();
            } else {
                inventoryScreen.hide();
            }
            //game.setScreen(marketScreen);
        }
        if (isInventoryVisible) {
            inventoryScreen.render(delta);
        }

        //inventory Eqip UI
        inventoryUI.drawInventory(player);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            inventoryUI.slotCol = 0;
            player.slotCursor = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            inventoryUI.slotCol = 1;
            player.slotCursor = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            inventoryUI.slotCol = 2;
            player.slotCursor = 2;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            inventoryUI.slotCol = 3;
            player.slotCursor = 3;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            inventoryUI.slotCol = 4;
            player.slotCursor = 4;
        }

        // bấm F để bắt đầu câu cá, nếu ItemName trà về tại vị trí SlotCursor của equipInventory == "Coin"
        // thì bắt đầu câu cá
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (player.eqipInventory.get(player.slotCursor).getItemName() == "Coin"){
                player.updateFishingAnimation();
            }
        }
    }
    private void renderSelectedCell() {
        if (player.currentActivity != PlayerAnimation.Activity.NONE) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 0, 0.3f); // Semi-transparent yellow

            shapeRenderer.rect(
                selectedCell.x * 16,
                selectedCell.y * 16,
                16,
                16
            );

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        if (selectedCell != null) {

        }
    }
    private void renderAmbientLighting() {

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(clock.getAmbientLighting());
        Matrix4 mat = camera.combined.cpy();
        shapeRenderer.setProjectionMatrix(mat);
        mat.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.rect(camera.position.x - viewport.getWorldWidth() / 2,
            camera.position.y - viewport.getWorldHeight() / 2,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);
    }
    private void renderPlayer() {
        game.batch.setProjectionMatrix(camera.combined);
        player.render(game.batch);
    }

    private void renderDebugInfo() {
        if (!showDebugInfo) return;

        game.batch.begin();
        font.draw(game.batch, String.format("Time: %s", timer.getFormattedTimeofDay()), 10, 470);
        font.draw(game.batch, String.format("Day passed: %s", timer.getDaysPassed()), 10, 410);
        font.draw(game.batch, String.format("CurrentDay : %s", currentDays), 10, 390);
        Color ambient = clock.getAmbientLighting();
        font.draw(game.batch, String.format("Ambient: R%.2f G%.2f B%.2f A%.2f", ambient.r, ambient.g, ambient.b, ambient.a), 10, 430);
        game.batch.end();
    }

    public Timer_ getTimer() {
        return timer;
    }


    public int getCurrentDays() {
        return currentDays;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        player.dispose();
        map.dispose();
        mapRenderer.dispose();
    }

    public void printAnimalDebugInfo() {
        game.batch.begin();
        String debugInfo = "Animal: " + animal.getType() +
            ",\n Hunger: " + animal.getHunger() +
            "\n, Health: " + animal.getHealth() + "\n, State: " + animal.getState()
            + "\n, can be produce?: " + animal.canBeProduced();

        font.draw(game.batch, debugInfo, player.getPosition().x, player.getPosition().y);
        font.draw(game.batch, player.getPosition().x +  " " + player.getPosition().y, player.getPosition().x + 100, player.getPosition().y + 100);
        game.batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) { return false;}

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            // Convert screen coordinates to world coordinates
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);

            // Convert world coordinates to cell coordinates
            int cellX = (int) (touchPosition.x / 16);
            int cellY = (int) (touchPosition.y / 16);

            // Update selected cell
            if (selectedCell == null) {
                selectedCell = new Vector2(cellX, cellY);
            } else {
                selectedCell.set(cellX, cellY);
            }
            Vector2 touchPosition2D = new Vector2(touchPosition.x, touchPosition.y);
            System.out.println(player.getPosition());
            System.out.println(touchPosition);
            if (player.getPosition().dst(touchPosition2D) <= 50) player.updateActivityAnimation(touchPosition2D);
            return true;
        }
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            player.stopActivity();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
