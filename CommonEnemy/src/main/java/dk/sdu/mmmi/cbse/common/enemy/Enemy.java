package dk.sdu.mmmi.cbse.common.enemy;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.Damageable;

public class Enemy extends Entity implements Damageable {


    private int health = 2;

    @Override
    public void damage(int amount) {
        health -= amount;
    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }
}

