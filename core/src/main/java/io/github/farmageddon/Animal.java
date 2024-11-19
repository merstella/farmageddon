package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Animal extends Actor {
    private Vector2 position;

    private String type;
    private float hunger;
    private boolean canProduce;
    private int state;
    private boolean isDead;

    private float currentHealth;
    private final float maxHealth = 100f;
    private Stage stage;
    private ProgressBar healthBar;

    private Texture texture;

    public Animal(float x, float y, String type, Stage stage) {
        position = new Vector2(x, y);

        this.type = type;
        this.hunger = 0;
        this.canProduce = false;
        this.state = 0;
        this.isDead = false;

        this.currentHealth = 100;
//        // init style
//        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("healthbar.atlas"));
//        Skin skin = new Skin(Gdx.files.internal("healthbarskin.json"));
//        skin.addRegions(atlas);
//
//        healthBar = new ProgressBar(0, maxHealth, 1, false, skin);
//        healthBar.setValue(currentHealth);
//        healthBar.setSize(100, 20);  // Set width and height as per your requirement
//        healthBar.setPosition(position.x, position.y + 10);

//        stage.addActor(this);
//        stage.addActor(healthBar);

        this.texture = new Texture("chickencutted/tile000.png");
    }

    public void update(float delta) {
        hunger += delta * 5;
        if (hunger > 100) {
            hunger = 100;
        }
        if (currentHealth == 0) isDead = true;
        if (hunger > 80) {
            currentHealth -= delta * 2;
        }
        if (currentHealth >= 80 && state == 3) {
            canProduce = true;
//            produce();
        } else canProduce = false;

//        healthBar.setValue(currentHealth);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        healthBar.setPosition(position.x, position.y + 50);
    }

    public void incState() {
        state += 1;
        if (state > 3) state = 3;
    }

    public void feed() {
        hunger = 0;
        currentHealth += 10;
        if (currentHealth > 100) currentHealth = 100;
    }

    public float getHunger() {
        return hunger;
    }

    public float getHealth() {
        return currentHealth;
    }

    public int getState() {
        return state;
    }

    public boolean canBeProduced() {
        return canProduce;
    }

    public String getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }

    public void produce() {
        System.out.println("Has produced!");
    }

}
