package io.github.farmageddon.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.farmageddon.*;

//import io.github.farmageddon.Crops.Crop;
import io.github.farmageddon.Crops.Crop;
import io.github.farmageddon.Crops.Land;
import io.github.farmageddon.Crops.LandManager;
//import io.github.farmageddon.markets.Market;
import io.github.farmageddon.entities.*;
import io.github.farmageddon.ultilites.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static com.badlogic.gdx.graphics.Color.WHITE;

public class GameScreen implements Screen, InputProcessor{
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
    private boolean showDebugInfo = false;
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
    public Animator.Activity currentActivity;
    private Animator animation;

    // Items Texture
    public Texture CoinTexture;
    public Texture bucketTexture;
    public Texture FishTexture;
    public Items items;
    public static Items Fish;

//    fishing minigame
    public FishingMinigame minigame;

    private Music music;

    private ArrayList<Animal> animals;
    private Stage stage;

    private Vector2 selectedCell;
    int currentDays;

    //demo crops
//    Crop riceCrop;
//    Crop tomatoCrop;

    // demo house
    Array<Entity> houses;

    // demo land
    private LandManager landManager;
//    private Land singleLand;

    public static Array<DroppedItem> droppedItems;

    public static boolean cursorLeft, cursorRight;
//    private Queue<Pair<Animal, Animal>> breedingQueue = new LinkedList<>();

//    private Monster monster;
//    private Plants specialPlant;
//

    public GameScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Main.GAME_WIDTH, Main.GAME_HEIGHT, camera);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        camera.zoom = .25f;
        camera.update();
        map = new TmxMapLoader().load("mapok.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        stage = new Stage(viewport);
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font.setColor(WHITE);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        selectedCell = new Vector2();

        timer = new Timer_();
        timer.setTimeRatio(20000);
        timer.StartNew(120, true, true);
        timer.setStartTime(0, 12, 30, 0);
        clock = new GameTimeClock(timer);
        time = timer.getFormattedTimeofDay();
        currentDays = 0;
        daysLeft = 30;
        initHouses();
        landManager = new LandManager(Main.GAME_HEIGHT/16,Main.GAME_WIDTH/16);
        player = new Player(640, 300, 100f);

//        market = new Market(100, 100, 200);
//        initPlayerInv();

        initAnimal();
//        monster = new Monster();
//        specialPlant = new Plants();
        droppedItems = new Array<>();
        initDebug();
//        music = Main.manager.get("Sound/music.mp3", Music.class);
//        music.setLooping(true);
//        music.setVolume(0.2f);
//        music.play();

        // torch
//        torch = new TorchLightHandler(world);
    }

    public void initMarket() {
        Texture defaultTexture = new Texture(Gdx.files.internal("default.png"));
        Default = new Items(defaultTexture,Items.ItemType.DEFAULT,Items.Item.DEFAULT, 0);
        while (player.eqipInventory.size() <= player.maxEqipInventorySize) {
            player.eqipInventory.add(Default);
        }
    }


    private void initAnimal() {
//        animal = new Animal(680, 230, "Chicken", stage);
//        animal.setBound(620, 690, 160, 260);
        animals = new ArrayList<>();
        Animal chicken1 = new Animal(650, 180, "Chicken", stage);
        chicken1.setBound(620, 690, 160, 260); // Set boundaries

        Animal chicken2 = new Animal(670, 200, "Chicken", stage);
        chicken2.setBound(620, 690, 160, 260); // Set boundaries
        animals.add(chicken1);
        animals.add(chicken2);

    }

    private void initHouses() {
        houses = new Array<>();
        MapObjects objects = map.getLayers().get("Nhà").getObjects();
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                float x = rect.x;
                float y = rect.y;
                Entity house = new Entity(x, y, 0, true, 100);
                houses.add(house);
            }
        }
    }

    public void initPlayerInv() {
        FishTexture = new Texture(Gdx.files.internal("Animals\\Bee\\Bee_Hive.png"));
        Fish = new Items(FishTexture, Items.ItemType.FOOD, Items.Item.FISH,10);

        CoinTexture = new Texture(Gdx.files.internal("Coin_Icon.png"));
        items = new Items(CoinTexture, Items.ItemType.OTHER, Items.Item.COIN, 10);
        player.setEquipItem(items);

        bucketTexture = new Texture(Gdx.files.internal("toolcutted/bucket.png"));
        items = new Items(bucketTexture, Items.ItemType.TOOL, Items.Item.BUCKET, 10);
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
//        inventoryUI = new InventoryUI(titleSize);
//        marketScreen = new MarketScreen(titleSize,market, player);
//        inventoryScreen = new InventoryScreen(titleSize, player);

        // fishing minigame
        // fishing minigame
//        minigame = new FishingMinigame();
//        minigame.create();
    }

    boolean isNewDay;
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
        game.batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);
        mapRenderer.render();
        isNewDay = false;
        handleDayPassed();
        clock.act(delta);


        checkBreeding();
        stage.act(delta);
        stage.draw();

        time = timer.getFormattedTimeofDay();
        timeLabel.setText(time);
        shapeRenderer.setProjectionMatrix(camera.combined);
        renderHouse();
        renderLand(delta);
//        renderAnimal();
        // renderplant, render monster
        renderPlayer();
        renderSelectedCell();
        handleCrop();
        renderCrop();
        //update all the thing before render minimap

        checkItemPickup(player);
        renderAmbientLighting();
        renderDebugInfo();
//        printAnimalDebugInfo();
        CollisionHandling.renderCollision();
        isNewDay = false;

//        handleKeyDown(delta);
    }

    private void checkBreeding() {
        // Create a copy of the current stage actors to avoid concurrent modification
        List<Animal> stageAnimals = new ArrayList<>();
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Animal) {
                stageAnimals.add((Animal) actor);
            }
        }

        for (int i = 0; i < stageAnimals.size(); i++) {
            Animal animalA = stageAnimals.get(i);

            for (int j = i + 1; j < stageAnimals.size(); j++) {
                Animal animalB = stageAnimals.get(j);

                float distance = animalA.getPosition().dst(animalB.getPosition());

                if (animalA.isEligibleForBreeding(animalB) && distance < 50) {
                    Animal offspring = animalA.breed(animalB, stage);
                    if (offspring != null) {
//                        System.out.println("Breeding "+ "Offspring Created: " + offspring.getType());
//                        System.out.println("Breeding "+ "Offspring Position: x=" + offspring.getX() + ", y=" + offspring.getY());

                        // Add offspring to both the stage and your list
                        animals.add(offspring);

                    }
                }
            }
        }
    }
    private void renderLand(float delta) {
        landManager.update(delta);
        game.batch.begin();
        for (int i = 0; i < Main.GAME_HEIGHT/16; i++) {
            for (int j = 0; j < Main.GAME_WIDTH/16; j++) {
                Land land = landManager.getLand(i, j);
                Land.LandState currentState = land.getCurrentState();
                Crop crop = land.getCrop();


                if (crop != null && isNewDay) {
                    crop.addDay();
                }

                if (currentState != Land.LandState.PLAIN) {
                    Texture texture = Land.getTextureForState(currentState);
                    game.batch.draw(texture,j*16 , i * 16, 16, 16);
                }
                if (crop != null) {
                    game.batch.draw(crop.getCurrentFrame(), j * 16, i * 16); // Render crop at grid position
                }
            }
        }
        for (DroppedItem item : droppedItems) {
            item.render(game.batch);
        }
        game.batch.end();
    }

    private void renderHouse() {
        for (Entity house : houses) {
            house.render(game.batch);
        }
    }

    private void handleDayPassed() {
        if(timer.getDaysPassed() != currentDays) {
            currentDays = timer.getDaysPassed();
            isNewDay = true;
            if (currentDays % 2 == 0) {
                for (Animal animal : animals) {
                    animal.incrementAge(); // Increment the age of each animal daily

                    // Update state if conditions are met (e.g., every 2 days of age)
                    if (animal.getAge() % 2 == 0 && animal.getHealth() > 80 && animal.getState() < 3) {
                        animal.incState();
                    }
                }

            }
        }
    }

    private void handleCrop() {

    }
    private void renderCrop() {

    }

    private void handleKeyDown(float delta) {
        // bấm m để hiện lên cửa sổ market
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
            Player.slotCursor = 0;
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

        //    bấm F để bắt đầu câu cá, nếu ItemName trà về tại vị trí SlotCursor của equipInventory == "Coin"
        if ((player.getPosition().x < 896f && player.getPosition().x  > 887f && player.getPosition().y < 500f && player.getPosition().y > 480f) ||
            (player.getPosition().x < 881f && player.getPosition().x  > 872f && player.getPosition().y < 556f && player.getPosition().y > 518f) ||
            (player.getPosition().x < 898f && player.getPosition().x  > 892f && player.getPosition().y < 570f && player.getPosition().y > 562f)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                cursorRight = !cursorRight;
            }
        } else if ( (player.getPosition().x < 1028f && player.getPosition().x  > 1026f && player.getPosition().y < 587f && player.getPosition().y > 576f) ||
            (player.getPosition().x < 1045f && player.getPosition().x  > 1039f && player.getPosition().y < 572f && player.getPosition().y > 563f) ||
            (player.getPosition().x < 1064f && player.getPosition().x  > 1060f && player.getPosition().y < 549f && player.getPosition().y > 516f) ||
            (player.getPosition().x < 1041f && player.getPosition().x  > 1039f && player.getPosition().y < 514f && player.getPosition().y > 480f) ||
            (player.getPosition().x < 1049f && player.getPosition().x  > 1044f && player.getPosition().y < 498f && player.getPosition().y > 465f)){
            if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                cursorLeft = !cursorLeft;
            }
        }
        if (cursorRight == true) {
            isFishingVisible = !isFishingVisible;
            if (player.eqipInventory.get(player.slotCursor).getItem() == Items.Item.COIN) {
                // Bật hoạt ảnh START_FISHING
                currentActivity = Animator.Activity.START_FISHING_RIGHT;
                player.updateFishingAnimation();
            }
            cursorRight = false;
        }
        if (cursorLeft == true) {
            isFishingVisible = !isFishingVisible;
            if (player.eqipInventory.get(player.slotCursor).getItem()== Items.Item.COIN) {
                // Bật hoạt ảnh START_FISHING
                currentActivity = Animator.Activity.START_FISHING_LEFT;
                player.updateFishingAnimation();
            }
            cursorLeft = false;
        }
        if (isFishingVisible) {
            switch (currentActivity) {
                case START_FISHING_RIGHT:
                    // Kiểm tra nếu hoạt ảnh đã chạy xong
                    if (animation.actionAnimations[currentActivity.ordinal()].isAnimationFinished(animation.stateTime)) {
                        currentActivity = Animator.Activity.WAIT_FISHING_RIGHT;
                        System.out.println("Start fishing animation right completed. Waiting for minigame...");
                    }
                    break;
                case START_FISHING_LEFT:
                    // Kiểm tra nếu hoạt ảnh đã chạy xong
                    if (animation.actionAnimations[currentActivity.ordinal()].isAnimationFinished(animation.stateTime)) {
                        currentActivity = Animator.Activity.WAIT_FISHING_LEFT;
                        System.out.println("Start fishing animation left completed. Waiting for minigame...");
                    }
                    break;

                case WAIT_FISHING_LEFT:
                    minigame.render();

                    // Kiểm tra nếu minigame đã kết thúc
                    if (minigame.cursorGameOver == true) {
                        currentActivity = Animator.Activity.DONE_FISHING_LEFT;
                        System.out.println("Minigame finished. Moving to Done Fishing animation...");
                    }
                    break;

                case WAIT_FISHING_RIGHT:
                    minigame.render();

                    // Kiểm tra nếu minigame đã kết thúc
                    if (minigame.cursorGameOver == true) {
                        currentActivity = Animator.Activity.DONE_FISHING_RIGHT;
                        System.out.println("Minigame finished. Moving to Done Fishing animation...");
                    }
                    break;
                case DONE_FISHING_RIGHT:
                case DONE_FISHING_LEFT:
                    // Chạy hoạt ảnh DONE_FISHING (có thể thêm logic nếu cần)
                    if (animation.actionAnimations[currentActivity.ordinal()].isAnimationFinished(animation.stateTime)) {
                        System.out.println("Fishing process completed!");
                        currentActivity = Animator.Activity.NONE;
                        isFishingVisible = false;
                        Player.hasStartedFishing = false;
                        FishingMinigame.gameOver = false;
                        FishingMinigame.cursorGameOver = false;
                    }
                    break;

                default:
                    break;
            }
        }
    }
    private void renderSelectedCell() {
        if (player.currentActivity != Animator.Activity.NONE) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

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
        player.render(game.batch);
    }

    private void renderDebugInfo() {
        if (!showDebugInfo) return;

        game.batch.begin();
        font.draw(game.batch, String.format("Time: %s", timer.getFormattedTimeofDay()), player.getPosition().x, player.getPosition().y);
        font.draw(game.batch, String.format("Day passed: %s", timer.getDaysPassed()), player.getBounds().x, player.getPosition().y - 30);
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
    public OrthographicCamera getCamera() {
        return camera;
    }

    private void checkItemPickup(Player player) {
        for (int i = 0; i < droppedItems.size; i++) {
            DroppedItem item = droppedItems.get(i);
            if (player.getBounds().overlaps(item.getBounds())) { // Check collision
                System.out.println("Picked up: " + item.getItemType());
//                player.setItem(item.getItemType()); // Add to player's inventory
                droppedItems.removeIndex(i); // Remove the item from the list
                item.dispose(); // Dispose the item's texture
                break;
            }
        }
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
        String debugInfo = "Animal: " + animals.get(0).getType() +
            ",\n Hunger: " + animals.get(0).getHunger() +
            "\n, Health: " + animals.get(0).getHealth() + "\n, State: " + animals.get(0).getState()
            + "\n, can be produce?: " + animals.get(0).canBeProduced();

        font.draw(game.batch, debugInfo, player.getPosition().x, player.getPosition().y);
        font.draw(game.batch, player.getPosition().x +  " " + player.getPosition().y, player.getPosition().x + 100, player.getPosition().y + 100);
        game.batch.end();
    }
    private String currentAct = "";
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.H) {
            currentAct = "hoe";
        } else if (keycode == Input.Keys.J) {
            currentAct = "water";
        } else if (keycode == Input.Keys.K) {
            currentAct = "harvest";
        }
        return true;
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
//            // Convert screen coordinates to world coordinates
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            int cellX = (int) touchPosition.x / 16;
            int cellY = (int) touchPosition.y / 16;
            selectedCell.set(cellX, cellY);

            Vector2 touchPosition2D = new Vector2(touchPosition.x, touchPosition.y);
            if (player.getPosition().dst(touchPosition2D) <= 50) {
//                System.out.println(touchPosition2D);
//                System.out.println(player.getPosition());
                player.updateActivityAnimation(currentAct, touchPosition2D);
                Land land = landManager.getLand(cellY,cellX);

                if (Objects.equals(currentAct, "hoe")) {
                    land.hoe();
//                    System.out.println(land.getCurrentState());
                    if (land.isEmpty()) {
                        Crop crop = new Crop(Items.Item.RICE, cellX * 16, cellY * 16); // Example crop
                        land.plantCrop(crop);
                    }
//                    System.out.println(land.isEmpty());
                }
                else if (Objects.equals(currentAct, "water")){
                    land.water();
                    if (!land.isEmpty()) {
                        land.getCrop().setWatered(true);
                    }
//                    System.out.println(land.getCurrentState());
                } else if (Objects.equals(currentAct, "harvest")){
                    if (!land.isEmpty()) {
                        DroppedItem droppedItem = land.harvestCrop();
                        if (droppedItem != null) {
                            droppedItems.add(droppedItem);
//                            System.out.println("Item dropped: " + droppedItem.getItemType());
                        }
                    }
                }
            }
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
