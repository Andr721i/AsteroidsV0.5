package dk.sdu.mmmi.cbse.scoresystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.Score;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

public class ScoreSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        Score score = gameData.getScore();

        for (String event : gameData.getScoreEvents()) {
            switch (event) {
                case "ASTEROID_DESTROYED":
                    score.addPoints(100);
                    break;
                case "ENEMY_DESTROYED":
                    score.addPoints(200);
                    break;
                case "RESET_SCORE":
                    score.reset();
                    break;

            }
        }


        gameData.clearScoreEvents();
    }
}
