package io.github.farmageddon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import io.github.farmageddon.entities.*;
import io.github.farmageddon.ultilites.GridNode;
import io.github.farmageddon.ultilites.PathFinder;

public class LogicalEntities {


    private PathFinder pathFinder;
    private float timerLogic;
    private Pool<Projectile> projectilePool;
    public void setPathFinder(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
        timerLogic = 0;
    }

    public LogicalEntities () {
        int i = 1;
        timerLogic = 0;
        projectilePool = new Pool<Projectile>() {
            @Override
            protected Projectile newObject() {
                return new Projectile(0f, 0f, 0f, 0f, (Entity) null);
            }
        };
    }
    public void updateEntities(Array<Monster> monsters, Array<ProtectPlant> plants, Array<Projectile> projectiles, Array<HouseEntity> entities, Player player, float delta) {
        timerLogic += delta;

        for (int i = projectiles.size - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.update(delta);

            if (projectile.getIsHitTarget()) {
                projectiles.removeIndex(i);
                projectilePool.free(projectile);
            }
        }

        // **Update Monsters**
        for (int i = monsters.size - 1; i >= 0; i--) {
            Monster monster = monsters.get(i);

            if (monster.getHealth() <= 0) {
                monster.die();
            }
            if (monster.isDead() && monster.isMarkedForRemoval()) {
                monsters.removeIndex(i);
            }
            if (monster.isNullTarget()) {
                monster.setTypeTarget(-1);
            }
            if (monster.getTargetHealth() <= 0 || monster.getTypeTarget() == -1 || monster.getTypeTarget() == 2 && timerLogic >= 0.5) {
                boolean bb = isUpdateMonsterTarget(monster, plants, entities, player);
//                if(timerLogic >= 1) {
//                    timerLogic = 0;
                assignPathToMonster(monster, pathFinder);

            }
            System.out.println(monster.getTypeTarget());
            // Update monster logic
            monster.update(delta);

        }

        // Reset timer for pathfinding frequency
        if (timerLogic >= 0.5) timerLogic = 0;

        // **Update Plants**
        for (int i = plants.size - 1; i >= 0; i--) {
            ProtectPlant plant = plants.get(i);

            // Handle plant death
            if (plant.getHealth() <= 0) {
                plants.removeIndex(i);
                continue;
            }

            plant.update(delta);

            if (monsters.isEmpty()) continue;

            // Shooting logic
            if (plant.getIsShooting()) {
                plant.shooted();
                Monster target = findNearestMonster(plant, monsters, plant.getRange());

                if (target != null) {
                    Projectile projectile = projectilePool.obtain();
                    projectile.reset();
                    projectile.initialize(plant.getPosition().x, plant.getPosition().y, 100, 1, target);
                    projectiles.add(projectile);
                    if (plant.getTypePlant() == 1) {
                        projectile.setSlowPoint(plant.getAdditionState());
                    }
                }
            }
        }
        for (int i = entities.size - 1; i >= 0; i--) {

            HouseEntity houseEntity = (HouseEntity) entities.get(i);
            if (houseEntity.getHouse().getCurrentHealth() <= 0) {
//                System.out.println("CACC");
                entities.removeIndex(i);
            }
        }
//        System.out.println(entities.size);
    }


    private Monster findNearestMonster(ProtectPlant plant, Array<Monster> monsters, float range) {
        Monster nearestMonster = null;
        float minDistance = range * range; // Compare squared distances to avoid sqrt calculation

        for (Monster monster : monsters) {
            float distanceSquared = monster.getPosition().dst2(plant.getPosition());
            if (distanceSquared < minDistance) {
                nearestMonster = monster;
                minDistance = distanceSquared;
            }
        }

        return nearestMonster;
    }


    public void renderEntities (Array<Monster> monsters, Array<ProtectPlant> plants, Array<Projectile> projectiles, Array<HouseEntity> entities, SpriteBatch batch) {
        for (Monster monster : monsters) monster.render(batch);
        for (ProtectPlant plant : plants) plant.render(batch);
        for (Projectile projectile : projectiles) projectile.render(batch);
        for (HouseEntity entity : entities) entity.render(batch);
    }

    public ProtectPlant findNearestPlant(Monster monster, Array<ProtectPlant> plants) {
        ProtectPlant nearestPlant = null;
        float minDistance = Float.MAX_VALUE; // Start with a very large value

        for (ProtectPlant plant : plants) {
            float distance = monster.getPosition().dst(plant.getPosition()); // Distance between zombie and plant
            if (distance < minDistance) {
                minDistance = distance;
                nearestPlant = plant; // Update nearest plant
            }
        }
//        System.out.println(nearestPlant.getPosition());
        return nearestPlant;
    }

    public void assignPathToMonster(Monster monster, PathFinder pathFinder) {
        // Check if the zombie already has a target plant
        Vector2 targetPosition = monster.getTargetPosition();
        GridNode startNode = pathFinder.getGridNodeForEntity(monster.getPosition());
        GridNode endNode = pathFinder.getGridNodeForEntity(targetPosition);

        int startX = pathFinder.getGridX(startNode);
        int startY = pathFinder.getGridY(startNode);
        int endX = pathFinder.getGridX(endNode);
        int endY = pathFinder.getGridY(endNode);

        Array<GridNode> path = new Array<>();
        if (pathFinder.findPath(startX, startY, endX, endY, path) == PathFinder.FOUND) {
//                if(monster.getTypeTarget() == 2 && timerLogic <= 2)return;
//                timerLogic = 0;
            monster.setPath(path); // Assign the path to the zombie
        }

    }

    public boolean isUpdateMonsterTarget (Monster monster, Array<ProtectPlant> plants, Array<HouseEntity> entities, Player player) {
        int typeTarget = -1;
        Entity targetEntity = null;
        ProtectPlant targetPlant = null;
        Player targetPlayer = null;
        float minDis = Float.MAX_VALUE, dis;
        for (ProtectPlant plant : plants) {
            dis = monster.getPosition().dst(plant.getPosition());
            if (dis > minDis) continue;
            typeTarget = 0;
            targetPlant = plant;
            minDis = dis;
        }
        for (Entity entity : entities) {
            dis = monster.getPosition().dst(entity.getPosition());
            if (dis > minDis) continue;
            typeTarget = 1;
            targetEntity = entity;
        }
        dis = monster.getPosition().dst(player.getPosition());
        if (typeTarget == -1 || ((dis <= monster.getRange() || monster.isNowTargetPlayer()) && dis < minDis)) {
            typeTarget = 2;
            if(dis <= monster.getRange()) monster.setTimeSinceTargetPlayer(0);
            targetPlayer = player;
        }
        if (typeTarget != monster.getTypeTarget()) {
            monster.setTypeTarget(typeTarget);
            switch (typeTarget) {
                case -1:
                    return false;
                case 0:
                    monster.setTargetPlant(targetPlant);
                    break;
                case 1:
                    monster.setTargetEntity(targetEntity);
                    break;
                case 2:
                    monster.setTargetPlayer(targetPlayer);
                    break;
            }
            return true;
        }
        boolean onlyBoolean = false;
        switch (typeTarget) {
            case -1:
                return false;
            case 0:
                if(targetPlant != monster.getTargetPlant())onlyBoolean = true;
                monster.setTargetPlant(targetPlant);
                break;
            case 1:
                if(targetEntity != monster.getTargetEntity())onlyBoolean = true;
                monster.setTargetEntity(targetEntity);
                break;
            case 2:
                onlyBoolean = true;
                monster.setTargetPlayer(targetPlayer);
                break;
        }
        return onlyBoolean;
    }

}

