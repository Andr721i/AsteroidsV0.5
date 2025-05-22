module Collision {
    requires Common;
    requires CommonAsteroids;
    requires CommonBullet;
    requires CommonEnemy;
    requires Player;

    uses dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;

    provides dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
}
