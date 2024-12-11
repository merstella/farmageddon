package io.github.farmageddon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Projectile extends Entity implements Pool.Poolable {
    private float damagePoint;
    private int typeTarget;
    private Entity targetEntity;
    private Monster targetMonster;
    private ProtectPlant targetPlant;
    private boolean isHitTarget;
    private Texture projTexture;
    private boolean slowable;
    private float slowPoint;
    private static final Texture PROJ_TEXTURE = new Texture(Gdx.files.internal("SellButton.png"));

    public void setSlowPoint (float slowPoint) {this.slowable = true; this.slowPoint = slowPoint;}
    public float getSlowPoint () {return slowPoint;}

    public void setDamagePoint(float damagePoint) {
        this.damagePoint = damagePoint;
    }
    public void initialize(float x, float y, float speed, float damagePoint, Entity targetEntity) {
        this.position.set(x, y);
        this.speed = speed;
        this.damagePoint = damagePoint;
        this.targetEntity = targetEntity;
        this.typeTarget = 0;
        this.isHitTarget = false;
    }

    public void initialize(float x, float y, float speed, float damagePoint, Monster targetMonster) {
        this.position.set(x, y);
        this.speed = speed;
        this.damagePoint = damagePoint;
        this.targetMonster = targetMonster;
        this.typeTarget = 1;
        this.isHitTarget = false;
    }

    public void initialize(float x, float y, float speed, float damagePoint, ProtectPlant targetPlant) {
        this.position.set(x, y);
        this.speed = speed;
        this.damagePoint = damagePoint;
        this.targetPlant = targetPlant;
        this.typeTarget = 2;
        this.isHitTarget = false;
    }
    public Projectile (float x, float y, float speed, float damagePoint, Entity targetEntity) {
        super(x, y, speed, false, 1);
        projTexture = PROJ_TEXTURE;
    }
    public boolean getIsHitTarget () {return isHitTarget;}

    private final Vector2 tempVector = new Vector2();

    public Vector2 getTargetPosition() {
        switch (typeTarget) {
            case 0:
                return tempVector.set(targetEntity.position).add(16, 16);
            case 1:
                return tempVector.set(targetMonster.position).add(16, 16);
            case 2:
                return tempVector.set(targetPlant.position).add(16, 16);
            default:
                return tempVector.set(-1, -1);
        }
    }

    private void damageTarget () {
        switch (typeTarget) {
            case 0:
                targetEntity.takeDamage(damagePoint);
                break;

            case 1:
                targetMonster.takeDamage(damagePoint);
                break;

            case 2:
                targetPlant.takeDamage(damagePoint);
                break;
        }
    }

    private float getTargetHP () {
        switch (typeTarget) {
            case 0:
                if(targetEntity == null)return 0;
                return targetEntity.getHealth();
            case 1:
                if(targetMonster == null)return 0;
                return targetMonster.getHealth();
            case 2:
                if(targetPlant == null)return 0;
                return targetMonster.getHealth();
        }
        return -1;
    }

    public void update (float delta) {
        super.update(delta);
        Vector2 mov = new Vector2(0, 0);
        Vector2 targetPosition = getTargetPosition();
        mov.x = targetPosition.x - position.x;
        mov.y = targetPosition.y - position.y;
        float base = (float) Math.sqrt(mov.x * mov.x + mov.y * mov.y);
        if (base > 0) {
            mov.x /= base; // Chuẩn hóa vector
            mov.y /= base;
        }
        mov.x *= speed * delta;
        mov.y *= speed * delta;

        if (Math.abs(mov.x) >= Math.abs(targetPosition.x - position.x) && Math.abs(mov.y) >= Math.abs(targetPosition.y - position.y)) {
            isHitTarget = true;
        }

        if(getTargetHP() <= 0)isHitTarget = true;

        if(isHitTarget == true){
            damageTarget();
        }

        position.x += mov.x;
        position.y += mov.y;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(projTexture, position.x, position.y, 5, 5);
        batch.end();
    }

    @Override
    public void reset() {
        damagePoint = 0;
        targetEntity = null;
        targetMonster = null;
        targetPlant = null;
        isHitTarget = false;
        slowable = false;
        slowPoint = 0;
        speed = 0; // Reset speed
        // Do not reset position here; initialize handles position
    }


}

