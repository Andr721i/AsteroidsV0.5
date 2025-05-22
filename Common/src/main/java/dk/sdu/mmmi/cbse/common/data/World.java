package dk.sdu.mmmi.cbse.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();

    public String addEntity(Entity entity) {
        entityMap.put(entity.getID(), entity);
        return entity.getID();
    }

    public void removeEntity(String entityID) {
        entityMap.remove(entityID);
    }

    public void removeEntity(Entity entity) {
        if (entity != null) {
            entityMap.remove(entity.getID());
        }
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    @SafeVarargs
    public final <E extends Entity> List<Entity> getEntities(Class<E>... entityTypes) {
        List<Entity> result = new ArrayList<>();
        for (Entity entity : getEntities()) {
            for (Class<E> entityType : entityTypes) {
                if (entityType.isInstance(entity)) {
                    result.add(entity);
                    break;  // Avoid adding the same entity multiple times if multiple classes match
                }
            }
        }
        return result;
    }

    public Entity getEntity(String ID) {
        return entityMap.get(ID);
    }

    // <-- Added clear method here
    public void clear() {
        entityMap.clear();
    }
}
