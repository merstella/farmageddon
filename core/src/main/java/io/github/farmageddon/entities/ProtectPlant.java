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
    private boolean isShooting;
    private int typePlant;
    private float additionState;
    private float opacity;
    private boolean isPlanted;
    private final Animator animation;
    private Animator.MonsterActivity currentActivity;

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

        typePlant = 0;
        opacity = 0f;
        isPlanted = false;
        animation = new Animator();
        currentActivity = Animator.MonsterActivity.IDLE_DOWN;
    }
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
    public void setPlanted(boolean isPlanted) {
        this.isPlanted = isPlanted;
    }

    public void update (float delta) {
        fromLastShoot += delta * timeMul;
        if (fromLastShoot >= cooldown) {
            fromLastShoot = 0;
            isShooting = true;
        }
    }

    public void render (SpriteBatch batch) {
        batch.begin();
        batch.setColor(1f, 1f, 1f, opacity);
        animation.render(batch, position.x, position.y, currentActivity, GameScreen.stateTime);
        batch.setColor(1f, 1f, 1f, 1f);
        batch.end();
    }

    @Override
    public void dispose()  {
        super.dispose();
        animation.dispose();
    }

    public void incrementOpacity(float delta) {
        this.opacity += delta;  // Increment opacity by delta (gradually fade in)
        this.opacity = Math.min(this.opacity, 1f);  // Ensure opacity doesn't exceed 1
    }

}
