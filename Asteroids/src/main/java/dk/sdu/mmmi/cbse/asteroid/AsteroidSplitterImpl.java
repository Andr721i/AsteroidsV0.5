package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Random;

public class AsteroidSplitterImpl implements IAsteroidSplitter {

    private final Random r = new Random();

    @Override
    public void createSplitAsteroid(Entity e, World world) {
        float currentRadius = e.getRadius();

        // Only split if asteroid is big enough
        if (currentRadius < 5) return;

        int numNewAsteroids = 2 + r.nextInt(2); // 2 or 3
        for (int i = 0; i < numNewAsteroids; i++) {
            Asteroid smallAsteroid = new Asteroid();
            float newRadius = currentRadius / 2;

            smallAsteroid.setRadius(newRadius);
            smallAsteroid.setX(e.getX());
            smallAsteroid.setY(e.getY());
            smallAsteroid.setRotation(r.nextInt(360));

            // Set new shape (simplified)
            smallAsteroid.setPolygonCoordinates(
                    newRadius, 0,
                    newRadius / 2f, -newRadius * 1.2f,
                    -newRadius / 3f, -newRadius,
                    -newRadius, -newRadius / 2f,
                    -newRadius * 1.1f, newRadius / 2f,
                    -newRadius / 2f, newRadius,
                    newRadius / 3f, newRadius * 0.8f,
                    newRadius, newRadius / 3f
            );

            world.addEntity(smallAsteroid);
        }
    }
}
