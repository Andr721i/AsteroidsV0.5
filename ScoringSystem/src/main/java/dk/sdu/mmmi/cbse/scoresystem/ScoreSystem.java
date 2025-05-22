package dk.sdu.mmmi.cbse.scoresystem;

import dk.sdu.mmmi.cbse.common.data.*;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;


public class ScoreSystem implements IPostEntityProcessingService {

    private int destroyedAsteroidsLastFrame = 0;

    @Override
    public void process(GameData gameData, World world) {
        Score score = gameData.getScore();

        // Count how many asteroids were destroyed this frame
        int currentAsteroids = world.getEntities(Asteroid.class).size();

        // Track enemies destroyed this frame
        int currentEnemies = world.getEntities(Enemy.class).size();

        // Use a static variable or external tracking to know the previous count?
        // But simpler approach is to check which entities are removed inside collision detector
        // So instead, update score directly inside CollisionDetector when entities are removed
    }
}
