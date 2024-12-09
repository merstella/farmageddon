package io.github.farmageddon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.farmageddon.Main;


public class MainMenuScreen implements Screen {
    // constants for button dimensions
    private static final int PLAY_BUTTON_WIDTH = 200;
    private static final int PLAY_BUTTON_HEIGHT = 100;
    private static final int EXIT_BUTTON_WIDTH = 200;
    private static final int EXIT_BUTTON_HEIGHT = 100;

    final private Main game;

    private Texture UI_sheet;
    TextureRegion ButtonActive;
    TextureRegion ButtonInactive;
    Stage stage;

    private Viewport viewport;
    private Camera camera;
    private Skin skin;

    public MainMenuScreen(Main game) {
        this.game = game;
        // test phần nút bâm
        UI_sheet = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\UI_Buttons.png"));
        skin = new Skin();
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.8f);
        skin.add("default", font);

        ButtonActive = new TextureRegion(UI_sheet,0,(UI_sheet.getHeight() / 19),32 ,14);
        ButtonInactive = new TextureRegion(UI_sheet,33, (UI_sheet.getHeight() / 19),32 ,14);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = new TextureRegionDrawable(ButtonInactive); // Trạng thái bình thường
        buttonStyle.down = new TextureRegionDrawable(ButtonActive); // Trạng thái nhấn
        buttonStyle.fontColor = Color.BLACK;
        skin.add("default", buttonStyle);

        createMenuButtons(buttonStyle);
    }

    private void createMenuButtons(TextButton.TextButtonStyle buttonStyle) {
        // Tạo nút Play
        TextButton playButton = new TextButton("PLAY", buttonStyle);
        playButton.getLabel().setAlignment(Align.center); // Căn giữa chữ
        playButton.pad(20); // Thêm khoảng cách xung quanh chữ
        playButton.setSize(250, 120);
        playButton.setPosition((Gdx.graphics.getWidth()-250)/2,(Gdx.graphics.getHeight()-120)/4 );
        playButton.addListener(event -> {
            if (playButton.isPressed()) {
                System.out.println("Play button clicked");
                game.setScreen(new GameScreen(game));
            }
            return true;
        });

        // Tạo nút Exit
        TextButton exitButton = new TextButton("EXIT", buttonStyle);
        exitButton.setSize(250, 120);
        exitButton.getLabel().setAlignment(Align.center); // Căn giữa chữ
        exitButton.pad(20); // Thêm khoảng cách xung quanh chữ
        exitButton.setPosition(2*(Gdx.graphics.getWidth()-250)/3 +100, (Gdx.graphics.getHeight()-120)/4);
        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                System.out.println("Exit button clicked");
                Gdx.app.exit();
            }
            return true;
        });

        // Tạo nút Instructions
        TextButton instructionsButton = new TextButton("INSTRUCTION", buttonStyle);
        instructionsButton.setSize(250, 120);
        instructionsButton.getLabel().setAlignment(Align.center); // Căn giữa chữ
        instructionsButton.pad(20); // Thêm khoảng cách xung quanh chữ
        instructionsButton.setPosition((Gdx.graphics.getWidth()-250)/3 - 100, (Gdx.graphics.getHeight()-120)/4);
        instructionsButton.addListener(event -> {
            if (instructionsButton.isPressed()) {
                System.out.println("Instructions button clicked");
//                game.setScreen(new InstructionsScreen(game));
            }
            return true;
        });

        // Thêm nút vào Stage
        stage.addActor(playButton);
        stage.addActor(exitButton);
        stage.addActor(instructionsButton);
    }



    @Override
    public void show() {
        // No initialization required here
    }

    @Override
    public void render(float delta) {
        // Clear the screen with black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
//        viewport.update(width, height); // Update the viewport on resizing
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        UI_sheet.dispose();
        stage.dispose();
    }
}

//public class MainMenuScreen implements Screen {
//    // constants for button dimensions
//    private static final int PLAY_BUTTON_WIDTH = 200;
//    private static final int PLAY_BUTTON_HEIGHT = 100;
//    private static final int EXIT_BUTTON_WIDTH = 200;
//    private static final int EXIT_BUTTON_HEIGHT = 100;
//
//    final private Main game;
//    Texture playButtonActive;
//    Texture playButtonInactive;
//    Texture exitButtonActive;
//    Texture exitButtonInactive;
//
//    private Viewport viewport;
//    private Camera camera;
//
//    public MainMenuScreen(Main game) {
//        camera = new PerspectiveCamera();
//        viewport = new FitViewport(800, 480, camera); // FitViewport for screen size adjustments
//        this.game = game;
//        playButtonActive = new Texture(Gdx.files.internal("playButtonActive.png"));
//        playButtonInactive = new Texture(Gdx.files.internal("playButtonInactive.png"));
//        exitButtonActive = new Texture(Gdx.files.internal("exitButtonActive.png"));
//        exitButtonInactive = new Texture(Gdx.files.internal("exitButtonInactive.png"));
//    }
//
//    @Override
//    public void show() {
//        // No initialization required here
//    }
//
//    @Override
//    public void render(float delta) {
//        // Clear the screen with black color
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        // Get the screen width and height dynamically
//        int screenWidth = Gdx.graphics.getWidth();
//        int screenHeight = Gdx.graphics.getHeight();
//
//        // Calculate button positions to center them on the screen
//        int playButtonX = (screenWidth - PLAY_BUTTON_WIDTH) / 2;
//        int playButtonY = screenHeight / 2 + PLAY_BUTTON_HEIGHT; // position the play button above the center
//        int exitButtonX = (screenWidth - EXIT_BUTTON_WIDTH) / 2;
//        int exitButtonY = screenHeight / 2 - EXIT_BUTTON_HEIGHT; // position the exit button below the center
//
//        // Begin rendering the buttons
//        game.batch.begin();
//
//        // Draw the play button (check if it is hovered and clicked)
//        if (Gdx.input.getX() < playButtonX + PLAY_BUTTON_WIDTH && Gdx.input.getX() > playButtonX
//            && screenHeight - Gdx.input.getY() < playButtonY + PLAY_BUTTON_HEIGHT
//            && screenHeight - Gdx.input.getY() > playButtonY) {
//            game.batch.draw(playButtonActive, playButtonX, playButtonY, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
//            if (Gdx.input.isTouched()) {
//                this.dispose();
//                game.setScreen(new GameScreen(game)); // Switch to the game screen
//            }
//        } else {
//            game.batch.draw(playButtonInactive, playButtonX, playButtonY, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
//        }
//
//        // Draw the exit button (check if it is hovered and clicked)
//        if (Gdx.input.getX() < exitButtonX + EXIT_BUTTON_WIDTH && Gdx.input.getX() > exitButtonX
//            && screenHeight - Gdx.input.getY() < exitButtonY + EXIT_BUTTON_HEIGHT
//            && screenHeight - Gdx.input.getY() > exitButtonY) {
//            game.batch.draw(exitButtonActive, exitButtonX, exitButtonY, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
//            if (Gdx.input.isTouched()) {
//                Gdx.app.exit(); // Exit the application
//            }
//        } else {
//            game.batch.draw(exitButtonInactive, exitButtonX, exitButtonY, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
//        }
//
//        game.batch.end();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height); // Update the viewport on resizing
//    }
//
//    @Override
//    public void pause() {}
//
//    @Override
//    public void resume() {}
//
//    @Override
//    public void hide() {}
//
//    @Override
//    public void dispose() {
//        playButtonActive.dispose();
//        playButtonInactive.dispose();
//        exitButtonActive.dispose();
//        exitButtonInactive.dispose();
//    }
//}
