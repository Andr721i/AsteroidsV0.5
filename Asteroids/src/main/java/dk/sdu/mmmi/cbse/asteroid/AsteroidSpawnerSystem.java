package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;

public class AsteroidSpawnerSystem implements IEntityProcessingService {

    private float timeSinceLastSpawn = 0;
    private final float spawnInterval = 4f; // every 4 seconds
    private final Random r = new Random();

    @Override
    public void process(GameData gameData, World world) {
        timeSinceLastSpawn += gameData.getDelta();

        if (timeSinceLastSpawn >= spawnInterval) {
            timeSinceLastSpawn = 0;
            Entity asteroid = generateAsteroid(gameData);
            world.addEntity(asteroid);
        }

    }

    private Entity generateAsteroid(GameData gd) {
        Asteroid a = new Asteroid();
        int s = r.nextInt(10) + 5;

        a.setPolygonCoordinates(
                s, 0,
                s / 2f, -s * 1.2f,
                -s / 3f, -s,
                -s, -s / 2f,
                -s * 1.1f, s / 2f,
                -s / 2f, s,
                s / 3f, s * 0.8f,
                s, s / 3f
        );

        a.setX(r.nextFloat() * gd.getDisplayWidth());
        a.setY(r.nextFloat() * gd.getDisplayHeight());
        a.setRadius(s);
        a.setRotation(r.nextInt(360)); // random rotation
        return a;

    }

}
