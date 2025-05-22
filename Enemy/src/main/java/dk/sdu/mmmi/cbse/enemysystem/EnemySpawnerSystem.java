package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class EnemySpawnerSystem implements IEntityProcessingService {

    private float spawnTimer = 0;
    private final float spawnInterval = 6f;

    @Override
    public void process(GameData gameData, World world) {
        spawnTimer += gameData.getDelta();
        if (spawnTimer > spawnInterval) {
            spawnTimer = 0;
            Enemy e = EnemyFactory.createEnemy(gameData);
            world.addEntity(e);
        }
    }
}
