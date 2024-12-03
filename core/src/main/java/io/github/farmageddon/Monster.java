package io.github.farmageddon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.MonsterAnimation;

public class Monster {
    private MonsterAnimation monsterAnimation;
    private float x, y, speed;
    private float targetX, targetY;
    private float health;

    public float getX() { return x; }

    public float getY() { return y; }

    public float getSpeed() { return speed; }

    public float getHealth() { return health; }

    public void setHealth(float health) { this.health = health; }

    public float getTargetX() { return targetX; }

    public float getTargetY() { return targetY; }

    public Monster(float x, float y, float speed, float targetX, float targetY) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.targetX = targetX;
        this.targetY = targetY;
        this.health = 100;
        monsterAnimation = new MonsterAnimation();
    }

    public void update(float delta) {
        Vector2 mov = new Vector2(0, 0);
        mov.x = targetX - x;
        mov.y = targetY - y;
        float base = (float) Math.sqrt(mov.x * mov.x + mov.y * mov.y);
        if (base > 0) {
            mov.x /= base; // Chuẩn hóa vector
            mov.y /= base;
        }

        x += mov.x * speed * delta;
        y += mov.y * speed * delta;


    }


    public void render(SpriteBatch batch) {
        batch.begin();

        monsterAnimation.render(batch,640f,300f, MonsterAnimation.MonsterState.IDLE);
        batch.end();
    }

    public void dispose() {
        monsterAnimation.dispose();
    }
}
