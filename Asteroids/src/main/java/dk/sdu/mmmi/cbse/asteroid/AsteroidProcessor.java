package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class AsteroidProcessor implements IEntityProcessingService {

    private IAsteroidSplitter asteroidSplitter = new AsteroidSplitterImpl();

    @Override
    public void process(GameData gameData, World world) {
        float screenW = (float) gameData.getDisplayWidth();
        float screenH = (float) gameData.getDisplayHeight();

        for (Entity ent : world.getEntities(Asteroid.class)) {
            float angle = (float) ent.getRotation();
            float speed = 0.5f;

            float dx = (float) (Math.cos(Math.toRadians(angle)) * speed);
            float dy = (float) (Math.sin(Math.toRadians(angle)) * speed);

            float newX = (float) (ent.getX() + dx);
            float newY = (float) (ent.getY() + dy);

            ent.setX(newX);
            ent.setY(newY);

            if (newX < 0f) {
                ent.setX(newX + screenW);
            } else if (newX > screenW) {
                ent.setX(newX % screenW);
            }

            if (newY < 0f) {
                ent.setY(newY + screenH);
            } else if (newY > screenH) {
                ent.setY(newY % screenH);
            }
        }
    }

    public void setAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = asteroidSplitter;
    }

    public void removeAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = null;
    }
}
