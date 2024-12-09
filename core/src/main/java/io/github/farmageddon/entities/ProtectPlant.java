package io.github.farmageddon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.CollisionHandling;
import io.github.farmageddon.ultilites.Items;
import java.util.ArrayList;
import java.util.Objects;


public class ProtectPlant extends Entity{
    private float cooldown, fromLastShoot, range;
    private float timeMul;
    private Texture curTexture;
    private boolean isShooting;
    private int typePlant;
    private float additionState;


    public void setTypePlant (int typePlant) {this.typePlant = typePlant;}
    public void setAdditionState (float additionState) {this.additionState = additionState;}
    public int getTypePlant () {return typePlant;}
    public float getAdditionState () {return additionState;}

    public float getRange () {
        return range;
    }
    public float getFromLastShoot () {
        return fromLastShoot;
    }
    public boolean getIsShooting () {return isShooting;}

    public void shooted () {this.isShooting = false;}

    @Override
    public Vector2 getPosition() {
        return super.getPosition();
    }
    public float getCooldown () {
        return cooldown;
    }

    public void setTimeMul (float timeMul) {this.timeMul = timeMul;}
    public void setFromLastShoot (float fromLastShoot) {this.fromLastShoot = fromLastShoot;}

    public ProtectPlant (float x, float y, float maxHealth) {
        super(x, y, 0f, true, maxHealth);
        cooldown = 1f;
        range = 100f;
        fromLastShoot = 0f;
        isShooting = false;
        timeMul = 1f;
        curTexture = new Texture("Well.png");
        typePlant = 0;
    }



//    public ProtectPlant (float x, float y, float maxHealth, int typePlant, float additionState) {
//        super(x, y, 0f, true, maxHealth);
//        cooldown = 1f;
//        range = 1000f;
//        fromLastShoot = 0f;
//        isShooting = false;
//        timeMul = 1f;
//        curTexture = new Texture("Well.png");
//        typePlant = 0;
//        this.typePlant = typePlant;
//        this.additionState = additionState;
//
//    }

    public void update (float delta) {
        fromLastShoot += delta * timeMul;
        if (fromLastShoot >= cooldown) {
            fromLastShoot = 0;
            isShooting = true;
        }
    }

    public void render (SpriteBatch batch) {
        batch.begin();
        batch.draw(curTexture, position.x, position.y, 10, 10);
        batch.end();
    }

}
