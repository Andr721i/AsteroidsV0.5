package dk.sdu.mmmi.cbse.playersystem;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.Damageable;

public class Player extends Entity implements Damageable {

    private int health = 3;
    @Override
    public void damage(int amount) {
        health -= amount;
    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }
}
