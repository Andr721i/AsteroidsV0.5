package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameState;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.playersystem.Player;
import dk.sdu.mmmi.cbse.common.data.Damageable;


import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class CollisionDetector implements IPostEntityProcessingService {

    private final IAsteroidSplitter asteroidSplitter;

    public CollisionDetector() {
        ServiceLoader<IAsteroidSplitter> loader = ServiceLoader.load(IAsteroidSplitter.class);
        this.asteroidSplitter = loader.findFirst().orElse(null);
    }

    @Override
    public void process(GameData gameData, World world) {
        if (asteroidSplitter == null) return;

        List<Entity> bulletsToRemove = new ArrayList<>();
        List<Entity> asteroidsToRemove = new ArrayList<>();
        List<Entity> enemiesToRemove = new ArrayList<>();
        List<Entity> playersToRemove = new ArrayList<>();


// Cast from Entity to specific classes
        List<Bullet> bullets = world.getEntities(Bullet.class).stream()
                .map(b -> (Bullet) b)
                .collect(Collectors.toList());

        List<Asteroid> asteroids = world.getEntities(Asteroid.class).stream()
                .map(a -> (Asteroid) a)
                .collect(Collectors.toList());

        List<Enemy> enemies = world.getEntities(Enemy.class).stream()
                .map(e -> (Enemy) e)
                .collect(Collectors.toList());

        List<Player> players = world.getEntities(Player.class).stream()
                .map(p -> (Player) p)
                .collect(Collectors.toList());


        // 1. Bullet-Asteroid collisions
        for (Bullet bullet : bullets) {
            for (Asteroid asteroid : asteroids) {
                if (collides(bullet, asteroid)) {
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                    asteroidSplitter.createSplitAsteroid(asteroid, world);
                }
            }
        }


        processBulletHitsEntities(bullets, enemies, bulletsToRemove, enemiesToRemove);
        processBulletHitsEntities(bullets, players, bulletsToRemove, playersToRemove);

        // 4. Enemy-Asteroid collisions (enemy destroyed on collision)
        for (Enemy enemy : enemies) {
            for (Asteroid asteroid : asteroids) {
                if (collides(enemy, asteroid)) {
                    enemiesToRemove.add(enemy);
                }
            }
        }

        // 5. Player-Asteroid collisions (player destroyed + game over)
        for (Player player : players) {
            for (Asteroid asteroid : asteroids) {
                if (collides(player, asteroid)) {
                    playersToRemove.add(player);
                    GameState.setGameOver(true);
                }
            }
        }

        // Remove all marked entities
        bulletsToRemove.forEach(world::removeEntity);
        asteroidsToRemove.forEach(world::removeEntity);
        enemiesToRemove.forEach(world::removeEntity);
        playersToRemove.forEach(world::removeEntity);
    }

    /**
     * Helper method to process bullet collisions with entities (enemies or players).
     * Bullets and entities that are hit are added to removal lists.
     * Entities are damaged, and removed if dead.
     */
    private <T extends Entity & Damageable> void processBulletHitsEntities(
            List<Bullet> bullets,
            List<T> entities,
            List<Entity> bulletsToRemove,
            List<Entity> entitiesToRemove) {

        for (Bullet bullet : bullets) {
            for (T entity : entities) {
                if (collides(bullet, entity)) {
                    bulletsToRemove.add(bullet);
                    entity.damage(1);
                    if (entity.isDead()) {
                        entitiesToRemove.add(entity);
                        if (entity instanceof Player) {
                            GameState.setGameOver(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Simple circle collision detection
     */
    public boolean collides(Entity e1, Entity e2) {
        float dx = (float) (e1.getX() - e2.getX());
        float dy = (float) (e1.getY() - e2.getY());
        float distSq = dx * dx + dy * dy;
        float radiusSum = e1.getRadius() + e2.getRadius();
        return distSq <= radiusSum * radiusSum;
    }


}
