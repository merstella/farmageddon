package io.github.farmageddon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.ultilites.GridNode;
import org.w3c.dom.Text;

public class Plant extends Entity{
    private boolean markedForRemoval = false;

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void markForRemoval() {
        markedForRemoval = true;
    }
    private Rectangle plantBounds;
    public Rectangle getPlantBounds() {
        return plantBounds;
    }
    public Plant(float x, float y, int maxHealth) {
        super(x, y, 0, true, maxHealth);
//        animation = new Animator();
//        currentActivity = Animator.MonsterActivity.IDLE_DOWN;
        plantBounds = new Rectangle(x + 7, y + 9, 14, 9);
//
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }
    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(new Texture(Gdx.files.internal("Coin_Icon.png")), position.x, position.y);
        batch.end();
    }
}
