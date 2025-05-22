package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class PlayerControlSystem implements IEntityProcessingService {

    // Time in seconds since last bullet was fired
    private float timeSinceLastShot = 0f;

    // Minimum time between shots (fire rate) - e.g., 0.25 seconds = 4 bullets per second
    private final float fireCooldown = 0.25f;

    @Override
    public void process(GameData gameData, World world) {
        // Update the timer with the delta time from last frame
        timeSinceLastShot += gameData.getDelta();

        for (Entity entity : world.getEntities(dk.sdu.mmmi.cbse.playersystem.Player.class)) {
            GameKeys keys = gameData.getKeys();

            int rotationSpeed = 5;
            float currentRotation = (float) entity.getRotation();
            if (keys.isDown(GameKeys.LEFT)) {
                entity.setRotation(currentRotation - rotationSpeed);
            }
            if (keys.isDown(GameKeys.RIGHT)) {
                entity.setRotation(currentRotation + rotationSpeed);
            }

            if (keys.isDown(GameKeys.UP)) {
                double radians = Math.toRadians(entity.getRotation());
                float newX = (float) (entity.getX() + Math.cos(radians));
                float newY = (float) (entity.getY() + Math.sin(radians));
                entity.setX(newX);
                entity.setY(newY);
            }

            if (keys.isDown(GameKeys.SPACE)) {
                // Only fire if enough time has passed since last shot
                if (timeSinceLastShot >= fireCooldown) {
                    getBulletSPIs().stream().findFirst().ifPresent(spi ->
                            world.addEntity(spi.createBullet(entity, gameData))
                    );
                    // Reset timer
                    timeSinceLastShot = 0f;
                }
            }

            float x = (float) entity.getX();
            float y = (float) entity.getY();
            int width = gameData.getDisplayWidth();
            int height = gameData.getDisplayHeight();

            if (x < 0) {
                entity.setX(1);
            } else if (x > width) {
                entity.setX(width - 1);
            }

            if (y < 0) {
                entity.setY(1);
            } else if (y > height) {
                entity.setY(height - 1);
            }
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }
}
