package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.List;
import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {

    @Override
    public void start(GameData gd, World w) {
        Entity e = generate(gd); // entity initialization
        w.addEntity(e);
    }

    @Override
    public void stop(GameData gd, World w) {
        List<Entity> group = w.getEntities(Asteroid.class); // batch removal
        for (int i = 0; i < group.size(); i++) {
            w.removeEntity(group.get(i));
        }
    }

    private Entity generate(GameData gd) {
        Asteroid a = new Asteroid();
        Random r = new Random();
        int s = r.nextInt(10) + 5;

        a.setPolygonCoordinates(
                s, 0,
                s / 2f, -s * 1.2f,
                -s / 3f, -s,
                -s, -s / 2f,
                -s * 1.1f, s / 2f,
                -s / 2f, s,
                s / 3f, s * 0.8f,
                s, s / 3f
        );

        a.setX(0);
        a.setY(0);
        a.setRadius(s);
        a.setRotation(r.nextInt(90)); // random rotation
        return a;
    }
}
