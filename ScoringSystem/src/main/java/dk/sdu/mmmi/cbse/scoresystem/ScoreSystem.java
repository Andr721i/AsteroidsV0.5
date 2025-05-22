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

        int currentAsteroids = world.getEntities(Asteroid.class).size();


        int currentEnemies = world.getEntities(Enemy.class).size();

    }
}
