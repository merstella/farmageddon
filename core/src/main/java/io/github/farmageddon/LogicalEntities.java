package io.github.farmageddon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.entities.*;
import io.github.farmageddon.ultilites.GridNode;
import io.github.farmageddon.ultilites.PathFinder;

public class LogicalEntities {
    private PathFinder pathFinder;

    public void setPathFinder(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public LogicalEntities () {
        int i = 1;
    }
    public void updateEntities (Array<Monster> monsters, Array<ProtectPlant> plants, Array<Projectile> projectiles, Array<Entity> entities, Player player, float delta) {
        for (int i = projectiles.size - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.update(delta);
            if (projectile.getIsHitTarget()) {
//                System.out.println(projectiles.size);
                projectiles.removeIndex(i);
            }
        }
        for (int i = monsters.size - 1; i >= 0; i--) {
            Monster monster = monsters.get(i);
            if(monster.getTargetHealth() <= 0){
                monster.setTypeTarget(-1);
            }
            if (monster.getHealth() <= 0) {
                monsters.removeIndex(i);
                continue;
            }
            if (monster.getTypeTarget() == -1) {
                boolean bb = isUpdateMonsterTarget(monster, plants, entities, player);
                assignPathToMonster(monster, pathFinder);
            }
            monster.update(delta);
        }
        for (int i = plants.size - 1; i >= 0; i--) {
            ProtectPlant plant = plants.get(i);
            if(plant.getHealth() <= 0){
                plants.removeIndex(i);
                continue;
            }
            plant.update(delta);
            if(monsters.size == 0)continue;
            if (plant.getIsShooting()){
                plant.shooted();
                float range = plant.getRange();
                float minDis = range + 1, dis = 0;
                Monster target = new Monster(0, 0, 0, 0);
                for (Monster monster : monsters) {
                    dis = (monster.getPosition().x - plant.getPosition().x) * (monster.getPosition().x - plant.getPosition().x) +
                        (monster.getPosition().y - plant.getPosition().y) * (monster.getPosition().y - plant.getPosition().y);
                    dis = (float)Math.sqrt(dis);
                    if(dis > minDis)continue;
                    target = monster;
                    minDis = dis;
                }
                if(minDis == range + 1){
                    plant.setFromLastShoot(plant.getCooldown() - delta);
                    continue;
                }
                projectiles.add(new Projectile(plant.getPosition().x, plant.getPosition().y, 100, 100, target));
                switch (plant.getTypePlant()) {
                    case 0:
                        break;
                    case 1:
                        projectiles.get(projectiles.size - 1).setSlowPoint(plant.getAdditionState());
                }
            }
        }
    }

    public void renderEntities (Array<Monster> monsters, Array<ProtectPlant> plants, Array<Projectile> projectiles, SpriteBatch batch) {
        for (Monster monster : monsters) monster.render(batch);
        for (ProtectPlant plant : plants) plant.render(batch);
        for (Projectile projectile : projectiles) projectile.render(batch);
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
                monster.setPath(path); // Assign the path to the zombie
            }
//            Plant currentTarget = monster.getTargetPlant();
//
//            // If the target plant is null or disposed, find a new target
//            if (currentTarget == null || currentTarget.isMarkedForRemoval()) {
//                // Check if there are any plants left to target
//                if (plants.size > 0) {
//                    // Find the nearest valid plant
//                    Plant nearestPlant = findNearestPlant(zombie, plants);
//                    if (nearestPlant != null && !nearestPlant.isMarkedForRemoval()) {
//                        // Find a path to the nearest plant
//                        GridNode startNode = pathFinder.getGridNodeForEntity(zombie.getPosition());
//                        GridNode endNode = pathFinder.getGridNodeForEntity(nearestPlant.getPosition());
//
//                        int startX = pathFinder.getGridX(startNode);
//                        int startY = pathFinder.getGridY(startNode);
//                        int endX = pathFinder.getGridX(endNode);
//                        int endY = pathFinder.getGridY(endNode);
//
//                        Array<GridNode> path = new Array<>();
//                        if (pathFinder.findPath(startX, startY, endX, endY, path) == PathFinder.FOUND) {
//                            zombie.setPath(path); // Assign the path to the zombie
//                            zombie.setTargetPlant(nearestPlant); // Set the new target plant
//                        }
//                    }
//                } else {
//                    // If no plants left, make the zombie idle or stop movement
//                    zombie.setTargetPlant(null); // Set target to null as no plant is available
//                    zombie.updateIdleAnimation(); // Update idle animation or stop movement
//                }
//            }
    }

    public boolean isUpdateMonsterTarget (Monster monster, Array<ProtectPlant> plants, Array<Entity> entities, Player player) {
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
