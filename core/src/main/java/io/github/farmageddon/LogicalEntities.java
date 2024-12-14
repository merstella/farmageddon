package io.github.farmageddon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import io.github.farmageddon.entities.*;
import io.github.farmageddon.ultilites.GameDifficulty;
import io.github.farmageddon.ultilites.GridNode;
import io.github.farmageddon.ultilites.PathFinder;
import io.github.farmageddon.ultilites.PrepareMonsters;

public class LogicalEntities {


    private PathFinder pathFinder;
    private float timerLogic;
    private int dayLogical;
    private int numDay;
    private Pool<Projectile> projectilePool;
    private float timePerDay, timeStartSpawn, timeFinishSpawn, secondSinceStart;
    private int curNumMonsterReleased, numMonsterNeedRelease;
    private boolean hasChangedDay;
    private Monster baseTargetMonster;
//    private GameDifficulty gameDifficulty;
    private PrepareMonsters spawner;
//    private float amountThisDaySpawn, numMonsterSpawned;

    public void setPathFinder(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
        timerLogic = 0;
    }
//    public void setGameDifficulty (GameDifficulty gameDifficulty) {this.gameDifficulty = gameDifficulty;}
    public void setSpawner (PrepareMonsters spawner) {this.spawner = spawner;}

    public LogicalEntities () {
        int i = 1;
        timerLogic = 0;
        projectilePool = new Pool<Projectile>() {
            @Override
            protected Projectile newObject() {
                return new Projectile(0f, 0f, 0f, null);
            }
        };
        dayLogical = 0;
        secondSinceStart = 0;
        timePerDay = 360;
        hasChangedDay = true;
        numDay = 1;
        timeStartSpawn = 180;
        timeFinishSpawn = 240;
        hasChangedDay = false;
        curNumMonsterReleased = 1;
        numMonsterNeedRelease = 1;
        baseTargetMonster = new Monster(0, 0, 0, 0);
    }
    public void increaseTimerLogic (float delta) {this.timerLogic += delta;}

    public String checkTimeType(float time) {
        if(time < timeStartSpawn) return "Farming time";
        if(time < timeFinishSpawn) return "Spawn and update";
        if(time <= timePerDay) return "Update";
        return "Not giving";
    }
    public void updateEntities(Array<Monster> monsters, Array<ProtectPlant> plants, Array<Projectile> projectiles, Array<HouseEntity> entities, Player player, float delta) {
        if(secondSinceStart <= 0.1) {
            while(monsters.size > 0)monsters.removeIndex(0);
        }
        if(monsters.size == 0){
            spawner.formatMonstes(monsters);
        }
        timerLogic += delta;
        secondSinceStart += delta;
        float timeThisDay = secondSinceStart % timePerDay;
        for (int i = projectiles.size - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.update(delta);

            if (projectile.getIsHitTarget()) {
                projectiles.removeIndex(i);
                projectilePool.free(projectile);
            }
        }

        String whatToDo = checkTimeType(timeThisDay);
//        System.out.println(whatToDo);
        switch (whatToDo) {
            case "Farming time":
                // this time will split time for spawn zombie
                // spawn and add to monsters but not release
//                if(spawner.isArrayNull())spawner.prepareArrays();
                if(hasChangedDay) {
//                    monsters.get(0).die();
//                    monsters.removeIndex(0);
                    for(Monster monster : monsters) {
//                        monster.die();
                        monster.setExist(false);
                        System.out.println("phong an");
                    }
                    dayLogical++;
                    hasChangedDay = false;
                    numDay += 1;
                    break;
                }
                if (curNumMonsterReleased != 0) {
                    curNumMonsterReleased = 0;

                    numMonsterNeedRelease = 10;

                    spawner.prepareMonsters(numMonsterNeedRelease, timeStartSpawn, timeFinishSpawn);
                    spawner.addMonstersToMonsters(numMonsterNeedRelease, monsters);
                    for(Monster monster : monsters) {
//                        System.out.println("monster not release: " + monster.getSpeed());
                    }
                    if(numDay % 5 == 0){
                        monsters.set(0, spawner.getBossState());
                    }
                }

                break;
            case "Spawn and update":
                hasChangedDay = true;
                System.out.println(numDay);
                // spawn and fall through to update
//                System.out.println(monsters.size);
                // spawn but only need release monster prepared
                if (curNumMonsterReleased < numMonsterNeedRelease) {
                    while (spawner.timeGreaterSummon(timeThisDay, curNumMonsterReleased) && curNumMonsterReleased < numMonsterNeedRelease) {
//                        spawner.releaseOrderMonster(curNumMonsterReleased);
                        monsters.get(curNumMonsterReleased).setExist(true);
//                        System.out.println(monsters.get(curNumMonsterReleased).getSpeed());
//                        System.out.println("RELEASE!!!!!!!!!!!!!!\n\n\n\n\n\n\n\n\n\n\n");
                        curNumMonsterReleased++;
                    }
                }

            case "Update":
//                System.out.println(monsters.size);
                for (int i = monsters.size - 1; i >= 0; i--) {
                    Monster monster = monsters.get(i);
//                    System.out.println("speed in update : " +monster.getSpeed());
                    if(!monster.isExist())continue;

                    if (monster.getHealth() <= 0) {
                        monster.die();
                    }
                    if (monster.isDead() && monster.isMarkedForRemoval()) {
                        monsters.get(i).setExist(false);
                        continue;
                    }
                    if(timerLogic >= 0.5) {
//                        monster.update(delta);
////                        System.out.println(monster.getPosition().x + " " + monster.getPosition().y);
//                        continue;
//                    }
                        //                    System.out.println(monster.getTargetHealth());
                        if (monster.isNullTarget() || monster.getTargetHealth() <= 0) {
                            monster.setTypeTarget(-1);
                        }
                        if ((monster.getTypeTarget() == -1 || monster.getTypeTarget() == 2) && timerLogic >= 0.5) {
                            boolean up = isUpdateMonsterTarget(monster, plants, entities, player);
                            //                if(timerLogic >= 1) {
                            //                    timerLogic = 0;
                            if (up || monster.getTypeTarget() == 2) assignPathToMonster(monster, pathFinder);

                        }
                        //                    System.out.println(monster.getTypeTarget());
                        // Update monster logic
                    }
                    monster.update(delta);
                    if (monster.isAttacking()) {
                        monster.setAttacking(false);
                        switch (monster.getTypeMonster()) {
                            case 0:
                                monster.applyDamageToTarget();
                                break;
                            case 1:
                                Projectile projectile = projectilePool.obtain();
                                projectile.reset();
                                switch (monster.getTypeTarget()) {
                                    case -1:
                                        break;
                                    case 0:
                                        projectile.initialize(monster.getPosition().x + 5, monster.getPosition().y + 5, 100, monster.getDamagePoint(), monster.getTargetPlant());
                                        break;
                                    case 1:
                                        projectile.initialize(monster.getPosition().x + 10, monster.getPosition().y + 10, 100, monster.getDamagePoint(), monster.getTargetEntity());
                                        break;
                                    case 2:
                                        projectile.initialize(monster.getPosition().x + 10, monster.getPosition().y + 10, 100, monster.getDamagePoint(), monster.getTargetPlayer());
                                        break;

                                }
                                projectiles.add(projectile);
//                                System.out.println(projectile.getTypeTarget());
//                                if (plant.getTypePlant() == 1) {
//                                    projectile.setSlowPoint(plant.getAdditionState());
//                                }
                        }
                    }
                }
                break;
            case "Not giving":
                break;
        }


        if(timerLogic >= 0.5)timerLogic = 0;

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
                float range = plant.getRange();
                float minDis = range + 1, dis = 0;
                Monster target = baseTargetMonster;
                for (Monster monster : monsters) {
                    if(!monster.isExist())continue;
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
                projectiles.add(new Projectile(plant.getPosition().x, plant.getPosition().y, 100, plant));
                switch (plant.getTypePlant()) {
                    case 0:
                        break;
                    case 1:
                        projectiles.get(projectiles.size - 1).setSlowPoint(plant.getAdditionState());
                }
//                projectiles.add(new Projectile(plant.getPosition().x, plant.getPosition().y, 100, 100, target));
//                switch (plant.getTypePlant()) {
//                    case 0:
//                        break;
//                    case 1:
//                        projectiles.get(projectiles.size - 1).setSlowPoint(plant.getAdditionState());
//                }
//                Monster target = findNearestMonster(plant, monsters, plant.getRange());

                plant.setTargetPosition(target.getPosition());
                if (target != null) {
                    Projectile projectile = projectilePool.obtain();
                    projectile.reset();
                    if (plant.getCurrentActivity() == Animator.PlantActivity.SHOOT_LEFT) {
                        projectile.initialize(plant.getPosition().x + 7, plant.getPosition().y + 13, 100, 100, target);
                    }
                    if (plant.getCurrentActivity() == Animator.PlantActivity.SHOOT_RIGHT)
                        projectile.initialize(plant.getPosition().x + 22, plant.getPosition().y + 13, 100, 1000, target);
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
        for (Monster monster : monsters) if(monster.isExist()) monster.render(batch);
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

    public boolean isUpdateMonsterTarget(Monster monster, Array<ProtectPlant> plants, Array<HouseEntity> entities, Player player) {
        int typeTarget = -1;
        Entity targetEntity = null;
        ProtectPlant targetPlant = null;
        Player targetPlayer = null;
        float minDis = Float.MAX_VALUE, dis;

        // Find the nearest plant
        for (ProtectPlant plant : plants) {
            dis = monster.getPosition().dst(plant.getPosition());
            if (dis > minDis) continue;
            typeTarget = 0;
            targetPlant = plant;
            minDis = dis;
        }

        // Find the nearest entity
        for (Entity entity : entities) {
            dis = monster.getPosition().dst(entity.getPosition());
            if (dis > minDis) continue;
            typeTarget = 1;
            targetEntity = entity;
        }

        dis = monster.getPosition().dst(player.getPosition());
        if (typeTarget == -1 || ((dis <= monster.getRange() || monster.isNowTargetPlayer()) && dis < minDis)) {
            typeTarget = 2;
            if (dis <= monster.getRange()) monster.setTimeSinceTargetPlayer(0);
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
                if (targetPlant != monster.getTargetPlant()) onlyBoolean = true;
                monster.setTargetPlant(targetPlant);
                break;
            case 1:
                if (targetEntity != monster.getTargetEntity()) onlyBoolean = true;
                monster.setTargetEntity(targetEntity);
                break;
            case 2:
                onlyBoolean = true;
                monster.setTargetPlayer(targetPlayer);
                break;
        }
        return onlyBoolean;
    }


    public void updatePlaceMonsters (Array<Monster> monsters) {

    }



    public float getTimePerDay() { return timePerDay; }
    public void setTimePerDay(float timePerDay) { this.timePerDay = timePerDay; }

    public float getTimeStartSpawn() { return timeStartSpawn; }
    public void setTimeStartSpawn(float timeStartSpawn) { this.timeStartSpawn = timeStartSpawn; }

    public float getTimeFinishSpawn() { return timeFinishSpawn; }
    public void setTimeFinishSpawn(float timeFinishSpawn) { this.timeFinishSpawn = timeFinishSpawn; }

    public float getSecondSinceStart() { return secondSinceStart; }
    public void setSecondSinceStart(float secondSinceStart) { this.secondSinceStart = secondSinceStart; }

    public int getCurNumMonsterReleased() { return curNumMonsterReleased; }
    public void setCurNumMonsterReleased(int curNumMonsterReleased) { this.curNumMonsterReleased = curNumMonsterReleased; }

    public int getNumMonsterNeedRelease() { return numMonsterNeedRelease; }
    public void setNumMonsterNeedRelease(int numMonsterNeedRelease) { this.numMonsterNeedRelease = numMonsterNeedRelease; }

}

