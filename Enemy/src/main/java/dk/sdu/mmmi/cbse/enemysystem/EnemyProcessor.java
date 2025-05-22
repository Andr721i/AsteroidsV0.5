package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.playersystem.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ServiceLoader;

public class EnemyProcessor implements IEntityProcessingService {

    private BulletSPI bulletSPI;

    public EnemyProcessor() {
        ServiceLoader<BulletSPI> loader = ServiceLoader.load(BulletSPI.class);
        bulletSPI = loader.findFirst().orElse(null);
    }

    private final Map<Entity, Float> shootTimers = new HashMap<>();
    private final Map<Entity, Float> shootCooldowns = new HashMap<>();

    private final float turnSpeed = 5f;
    private final float speed = 0.3f;
    private final Random random = new Random();

    @Override
    public void process(GameData gameData, World world) {
        Entity player = world.getEntities(Player.class).stream().findFirst().orElse(null);
        if (player == null) {
            return;
        }

        for (Entity enemy : world.getEntities(Enemy.class)) {

            shootTimers.putIfAbsent(enemy, 0f);
            shootCooldowns.putIfAbsent(enemy, 8f + random.nextFloat() * 4f); // 8–12 sec

            float timer = shootTimers.get(enemy);
            float cooldown = shootCooldowns.get(enemy);

            timer += gameData.getDelta();
            shootTimers.put(enemy, timer);

            float enemyX = (float) enemy.getX();
            float enemyY = (float) enemy.getY();
            float playerX = (float) player.getX();
            float playerY = (float) player.getY();

            // Angle to player
            float angleToPlayer = (float) Math.toDegrees(Math.atan2(playerY - enemyY, playerX - enemyX));

            // Move directly toward player (no avoidance)
            float moveX = (float) Math.cos(Math.toRadians(angleToPlayer));
            float moveY = (float) Math.sin(Math.toRadians(angleToPlayer));

            float finalAngle = (float) Math.toDegrees(Math.atan2(moveY, moveX));
            float currentRotation = (float) enemy.getRotation();
            float rotationDifference = finalAngle - currentRotation;
            rotationDifference = ((rotationDifference + 180) % 360) - 180;

            enemy.setRotation(currentRotation + Math.signum(rotationDifference) * Math.min(Math.abs(rotationDifference), turnSpeed));

            float dx = (float) Math.cos(Math.toRadians(enemy.getRotation())) * speed;
            float dy = (float) Math.sin(Math.toRadians(enemy.getRotation())) * speed;
            enemy.setX(enemy.getX() + dx);
            enemy.setY(enemy.getY() + dy);

            wrapPosition(enemy, gameData);

            // Random shooting
            if (timer >= cooldown && bulletSPI != null) {
                Entity bullet = bulletSPI.createBullet(enemy, gameData);
                world.addEntity(bullet);

                shootTimers.put(enemy, 0f);
                shootCooldowns.put(enemy, 3f + random.nextFloat() * 9f);
            }
        }
    }

    private void wrapPosition(Entity e, GameData gd) {
        float w = gd.getDisplayWidth();
        float h = gd.getDisplayHeight();
        float x = (float) e.getX();
        float y = (float) e.getY();

        if (x < 0) e.setX(w);
        if (x > w) e.setX(0);
        if (y < 0) e.setY(h);
        if (y > h) e.setY(0);
    }

    public void setBulletSPI(BulletSPI bulletSPI) {
        this.bulletSPI = bulletSPI;
    }

    public void removeBulletSPI(BulletSPI bulletSPI) {
        if (this.bulletSPI == bulletSPI) {
            this.bulletSPI = null;
        }
    }
}
