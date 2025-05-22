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
    private GameData gameData;

    public CollisionDetector() {
        ServiceLoader<IAsteroidSplitter> loader = ServiceLoader.load(IAsteroidSplitter.class);
        this.asteroidSplitter = loader.findFirst().orElse(null);
    }

    @Override
    public void process(GameData gameData, World world) {
        if (asteroidSplitter == null) return;

        this.gameData = gameData;

        List<Entity> bulletsToRemove = new ArrayList<>();
        List<Entity> asteroidsToRemove = new ArrayList<>();
        List<Entity> enemiesToRemove = new ArrayList<>();
        List<Entity> playersToRemove = new ArrayList<>();

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


        for (Bullet bullet : bullets) {
            for (Asteroid asteroid : asteroids) {
                if (collides(bullet, asteroid)) {
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                    asteroidSplitter.createSplitAsteroid(asteroid, world);


                    gameData.getScore().addPoints(100);
                }
            }
        }


        processBulletHitsEntities(bullets, enemies, bulletsToRemove, enemiesToRemove);
        processBulletHitsEntities(bullets, players, bulletsToRemove, playersToRemove);

        for (Enemy enemy : enemies) {
            for (Asteroid asteroid : asteroids) {
                if (collides(enemy, asteroid)) {
                    enemiesToRemove.add(enemy);
                    gameData.getScore().addPoints(200);
                }
            }
        }


        for (Player player : players) {
            for (Asteroid asteroid : asteroids) {
                if (collides(player, asteroid)) {
                    playersToRemove.add(player);
                    GameState.setGameOver(true);

                    gameData.getScore().reset();
                }
            }
        }


        bulletsToRemove.forEach(world::removeEntity);
        asteroidsToRemove.forEach(world::removeEntity);
        enemiesToRemove.forEach(world::removeEntity);
        playersToRemove.forEach(world::removeEntity);
    }

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
                            gameData.getScore().reset();
                        } else if (entity instanceof Enemy) {
                            gameData.getScore().addPoints(200);
                        }
                    }
                }
            }
        }
    }

    public boolean collides(Entity e1, Entity e2) {
        float dx = (float) (e1.getX() - e2.getX());
        float dy = (float) (e1.getY() - e2.getY());
        float distSq = dx * dx + dy * dy;
        float radiusSum = e1.getRadius() + e2.getRadius();
        return distSq <= radiusSum * radiusSum;
    }
}
