package dk.sdu.mmmi.cbse.scoresystem;

import dk.sdu.mmmi.cbse.common.data.*;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.scoreserviceclient.ScoreClient;

import java.util.List;

public class ScoreSystem implements IPostEntityProcessingService {

    private ScoreClient scoreClient = new ScoreClient();

    // Track counts from previous frame
    private int destroyedAsteroidsLastFrame = 0;
    private int destroyedEnemiesLastFrame = 0;

    // Points awarded per destroyed asteroid/enemy
    private static final int ASTEROID_POINTS = 100;
    private static final int ENEMY_POINTS = 200;

    @Override
    public void process(GameData gameData, World world) {
        // Get current lists of asteroids and enemies
        List<Entity> asteroids = world.getEntities(Asteroid.class);
        List<Entity> enemies = world.getEntities(Enemy.class);

        int currentAsteroids = asteroids.size();
        int currentEnemies = enemies.size();

        // Calculate destroyed since last frame
        int destroyedAsteroids = destroyedAsteroidsLastFrame - currentAsteroids;
        int destroyedEnemies = destroyedEnemiesLastFrame - currentEnemies;

        // Only add points if something was destroyed
        if (destroyedAsteroids > 0) {
            gameData.getScore().addPoints(destroyedAsteroids * ASTEROID_POINTS);
        }

        if (destroyedEnemies > 0) {
            gameData.getScore().addPoints(destroyedEnemies * ENEMY_POINTS);
        }

        // Update last frame counts for next processing
        destroyedAsteroidsLastFrame = currentAsteroids;
        destroyedEnemiesLastFrame = currentEnemies;

        // Safely send updated score to ScoreClient
        int currentScore = gameData.getScore().getCurrentScore();
        if (scoreClient != null) {
            scoreClient.updateScore(currentScore);
        }
    }
}
