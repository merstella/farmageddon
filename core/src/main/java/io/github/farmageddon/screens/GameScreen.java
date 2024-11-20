package io.github.farmageddon.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.farmageddon.*;
import io.github.farmageddon.Crops.Crop;
import io.github.farmageddon.Crops.Items;
import io.github.farmageddon.Crops.Seeds;
import io.github.farmageddon.markets.Market;
import io.github.farmageddon.ultilites.GameTimeClock;
import io.github.farmageddon.ultilites.Timer_;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.badlogic.gdx.graphics.Color.WHITE;

public class GameScreen implements Screen, InputProcessor {
    private final Main game;

    private OrthographicCamera camera;
    private Viewport gameView;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private GameTimeClock clock;
    private Timer_ timer;
    private boolean showDebugInfo = true;
    private String time;
    private static Label timeLabel, timeStringLabel, daysLeftLabel, daysLeftNum;
    private static Label scoreLabel;
    private static Label scoreStringLabel;
    private static int daysLeft;

    private final Player player;
    private final Market market;
    private boolean isMarketVisible = false;

    private Music music;

    //Tool variables
    public Items.ItemType currentType;
    public Items currentItem;
    public Texture bucketTexture;
    public Items bucket;
    public Items riceSeed;
    public Items tomatoSeed;
    public Items carrotSeed;
    public Items cornSeed;
    public int intType;

    //Crop variables
    public Array<Crop> crops;
    public int numCrops;
    private TextureRegion[][] textureFrames;
    private TextureRegion mouseFrame;
    public Texture mouseCrop;
    public Array<Seeds> seeds;
    public static Integer money;

    // tiled map
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Array<Items> items;
    private Texture box;
    private Texture border;

    private Animal animal;
    private Stage stage;

    private static Vector2 selectedCell;

    int currentDays;
    public GameScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        gameView = new FitViewport(1280, 768, camera);
        camera.setToOrtho(false, gameView.getWorldWidth(), gameView.getWorldHeight());
        map = new TmxMapLoader().load("mapok.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        player = new Player(640, 384, 100f);
        camera.zoom = 0.25f;
        camera.update();


        seeds = new Array<Seeds>();

        market = new Market(game);


        font = new BitmapFont();
        font.setColor(WHITE);

        timer = new Timer_();
        timer.setTimeRatio(9000);
        timer.StartNew(120, true, true);
        timer.setStartTime(0, 12, 30, 0);
        clock = new GameTimeClock(timer);
        time = timer.getFormattedTimeofDay();
        currentDays = 0;
        daysLeft = 30;

        timeLabel = new Label(time, new Label.LabelStyle(font, WHITE));
        timeStringLabel = new Label("Time", new Label.LabelStyle(font, WHITE));
        timeStringLabel.setFontScale(2);
        timeLabel.setFontScale(2);
        daysLeftLabel = new Label("Days Left", new Label.LabelStyle(font, WHITE));
        daysLeftNum = new Label(String.format("%d", daysLeft), new Label.LabelStyle(font, WHITE));
        daysLeftLabel.setFontScale(2);
        daysLeftNum.setFontScale(2);
        scoreLabel = new Label(String.format("$%d", money), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreStringLabel = new Label("Money", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        stage = new Stage(gameView);

        shapeRenderer = new ShapeRenderer();
        money = 100;
        crops = new Array<>();
        numCrops = 0;
        crops.add(new Crop(Items.Item.RICE, 100, 100));
        crops.add(new Crop(Items.Item.TOMATO, 200, 200));

        animal = new Animal(680, 230, "Chicken", stage);
        animal.setBound(620, 690, 160, 260);

        intType = 0;
        mouseCrop = new Texture(Gdx.files.internal("Crops.png"));
        textureFrames = TextureRegion.split(mouseCrop, 32, 32);
        bucketTexture = new Texture("ref assets/bucket.png");
        bucket = new Items(bucketTexture, Items.ItemType.TOOL, Items.Item.BUCKET);
        riceSeed = new Items(textureFrames[0][1], Items.ItemType.SEED, Items.Item.RICE);
        tomatoSeed = new Items(textureFrames[1][1], Items.ItemType.SEED, Items.Item.TOMATO);
        carrotSeed = new Items(textureFrames[2][1], Items.ItemType.SEED, Items.Item.CARROT);
        cornSeed = new Items(textureFrames[4][1], Items.ItemType.SEED, Items.Item.CORN);

        items = new Array<Items>(5);
        items.add(bucket);
        items.add(riceSeed);
        items.add(tomatoSeed);
        items.add(carrotSeed);
        items.add(cornSeed);

        setMouseCrop(bucket);

        box = new Texture(Gdx.files.internal("box.png"));
        border = new Texture(Gdx.files.internal("border.png"));

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        selectedCell = new Vector2();

        for(int i = 0; i < 20; i++)
            addSeeds(carrotSeed.getItem());

        music = Main.manager.get("Sound/music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
    }

    @Override
    public void show() {
        //mapRenderer = new OrthogonalTiledMapRenderer(map);


    }

    @Override
    public void render(float delta) {
        setting();
        if(timer.getDaysPassed() != currentDays){
            for (Crop crops : crops)
                crops.addDay();
            currentDays = timer.getDaysPassed();
            if (currentDays % 2 == 0) animal.incState();
            daysLeft--;
            daysLeftNum.setText(String.format("%d", daysLeft));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            isMarketVisible = !isMarketVisible;
            if (isMarketVisible) {
                game.setScreen(market);
            } else {
                game.setScreen(this);
            }
        }
        player.update(delta);
        clock.act(delta);
        animal.update(delta, currentDays);
        stage.act();
        stage.draw();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            animal.feed();
        }
        printAnimalDebugInfo();


        time = timer.getFormattedTimeofDay();
        timeLabel.setText(time);

        renderSelectedCell();
        //.out.println(player.isPerformingActivity());
        renderCrops();
        renderPlayer();
        renderAmbientLighting();
        gameView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderDebugInfo();

    }
    private void setting() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(MathUtils.clamp(player.getPosition().x + 16,160,1120), MathUtils.clamp(player.getPosition().y + 16, 0, 768), 0);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);
        mapRenderer.render();
    }
    private void renderSelectedCell() {
        // Check if player is performing an activity
        if (player.isPerformingActivity()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rect(selectedCell.x * 16, selectedCell.y * 16, 16, 16);
            shapeRenderer.end();
        }
    }
    private void renderAmbientLighting() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(clock.getAmbientLighting());
        Matrix4 mat = camera.combined.cpy();
        shapeRenderer.setProjectionMatrix(mat);
        mat.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.rect(camera.position.x - gameView.getWorldWidth() / 2,
            camera.position.y - gameView.getWorldHeight() / 2,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);
    }
    private void renderPlayer() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch);
        game.batch.end();
    }
    private void renderCrops() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        for(int i = 0; i < numCrops; i++) {
            if (crops.get(i).isWatered())
                game.batch.setColor(Color.BROWN);

            game.batch.setColor(Color.WHITE);
            game.batch.draw(crops.get(i).getCurrentFrame(), crops.get(i).getFrameSprite().getX(), crops.get(i).getFrameSprite().getY());
            if (crops.get(i).isDead()) {
                crops.removeIndex(i);
                numCrops--;
            }
        }
        for(int i = 0; i < seeds.size; i++){
            game.batch.draw(seeds.get(i).getTexture(), seeds.get(i).getBoundingRect().x, seeds.get(i).getBoundingRect().y);
        }

        for(int i = 0; i < 9; i++) {
            game.batch.draw(box, (camera.position.x + 32 * i) - (camera.viewportWidth / 2 * (camera.zoom / 2)), camera.position.y - (camera.viewportHeight / 2 * camera.zoom));
            if(i < items.size) {
                game.batch.draw(items.get(i).getTextureRegion(), (camera.position.x + 32 * i) - (camera.viewportWidth / 2 * (camera.zoom / 2)), camera.position.y - (camera.viewportHeight / 2 * camera.zoom));
                if(items.get(i).getType() == Items.ItemType.SEED)
                    font.draw(game.batch, String.format("%d", items.get(i).getNum()), (camera.position.x + 32 * i) - (camera.viewportWidth / 2 * (camera.zoom / 2)-6), camera.position.y - (camera.viewportHeight / 2 * camera.zoom)+12);
                if (items.get(i).getItem() == currentItem.getItem())
                    game.batch.draw(border, (camera.position.x + 32 * i) - (camera.viewportWidth / 2 * (camera.zoom / 2)), camera.position.y - (camera.viewportHeight / 2 * camera.zoom));
            }
        }
        game.batch.end();
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

    public OrthographicCamera getCamera() {
        return camera;
    }

    public int getCurrentDays() {
        return currentDays;
    }
    public void setMouseCrop(Items item){
        mouseFrame = item.getTextureRegion();
        currentItem = item;
        currentType = item.getType();

    }

    public void buySeed(Seeds seed){
        if(seed.getPrice() <= money) {
            money -= seed.getPrice();
            addSeeds(seed.getItem());
        }
        scoreLabel.setText(String.format("$%d", money));
    }
    public void addMoney(int price){
        money += price;
        scoreLabel.setText(String.format("$%d", money));
    }
    public void addSeeds(Items.Item item) {
        switch(item) {
            case RICE:
                riceSeed.add();
                break;
            case TOMATO:
                tomatoSeed.add();
                break;
            case CORN:
                cornSeed.add();
                break;
            case CARROT:
                carrotSeed.add();
        }
    }

    public Array<Seeds> getSeeds() {
        return seeds;
    }
    public Array<Crop> getCrops() {
        return crops;
    }
    public Array<Items> getItems() {
        return items;
    }

    @Override
    public void resize(int width, int height) {}

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


    public void addCrop(Crop crop){
        crops.add(crop);
    }
    public void removeSeeds(Items.Item item) {
        switch(item) {
            case RICE:
                riceSeed.remove();
                break;
            case TOMATO:
                tomatoSeed.remove();
                break;

            case CORN:
                cornSeed.remove();
                break;
            case CARROT:
                carrotSeed.remove();
                break;
        }
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
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
            float worldX = worldCoords.x;
            float worldY = worldCoords.y;

            selectedCell.set(
                (int)(worldX / 16),
                (int)(worldY / 16)
            );
            player.startActivity(new Vector2(worldX, worldY));
            return true;
        }
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            player.stopActivity(); // Use the new method to stop activity
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
