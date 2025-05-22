module Enemy {
    requires Common;
    requires Bullet;
    requires CommonBullet;
    requires CommonEnemy;
    requires Player;
    requires CommonAsteroids;

    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;

    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService
            with dk.sdu.mmmi.cbse.enemysystem.EnemyProcessor,
                    dk.sdu.mmmi.cbse.enemysystem.EnemySpawnerSystem;

    exports dk.sdu.mmmi.cbse.enemysystem;
}
