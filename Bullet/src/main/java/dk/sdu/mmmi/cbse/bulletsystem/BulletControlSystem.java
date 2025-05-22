package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {


    private Map<Entity, Float> bulletLifetimes = new HashMap<>();

    @Override
    public void process(GameData gameData, World world) {

        Iterator<Entity> bulletIterator = world.getEntities(Bullet.class).iterator();
        while (bulletIterator.hasNext()) {
            Entity bullet = bulletIterator.next();

            // Move bullet forward
            double dx = Math.cos(Math.toRadians(bullet.getRotation()));
            double dy = Math.sin(Math.toRadians(bullet.getRotation()));
            bullet.setX(bullet.getX() + dx * 3);
            bullet.setY(bullet.getY() + dy * 3);


            float timeAlive = bulletLifetimes.getOrDefault(bullet, 0f);
            timeAlive += gameData.getDelta();
            bulletLifetimes.put(bullet, timeAlive);


            if (timeAlive > 6f) {
                world.removeEntity(bullet);
                bulletIterator.remove();
                bulletLifetimes.remove(bullet);
            }
        }
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Bullet();
        bullet.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);
        double dx = Math.cos(Math.toRadians(shooter.getRotation()));
        double dy = Math.sin(Math.toRadians(shooter.getRotation()));
        bullet.setX(shooter.getX() + dx * 10);
        bullet.setY(shooter.getY() + dy * 10);
        bullet.setRotation(shooter.getRotation());
        bullet.setRadius(1);


        bulletLifetimes.put(bullet, 0f);

        return bullet;
    }
}
