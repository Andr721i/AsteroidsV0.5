package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;

import java.util.Random;

public class EnemyFactory {

    public static Enemy createEnemy(GameData gameData) {
        Random rand = new Random();
        Enemy e = new Enemy();

        e.setX(rand.nextFloat() * gameData.getDisplayWidth());
        e.setY(rand.nextFloat() * gameData.getDisplayHeight());
        e.setRadius(10);
        e.setRotation(rand.nextInt(360));

        e.setPolygonCoordinates(
                -10, -10,
                10, 0,
                -10, 10
        );

        return e;
    }
}
