import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.playersystem.Player;
import dk.sdu.mmmi.cbse.playersystem.PlayerControlSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerControlSystemIntegrationTest {

    private PlayerControlSystem playerControlSystem;
    private GameData gameData;
    private World world;
    private Player player;

    @BeforeEach
    public void setup() {
        playerControlSystem = new PlayerControlSystem();

        gameData = new GameData();
        world = new World();
        player = new Player();


        player.setX(100);
        player.setY(100);
        player.setRotation(0);


        world.addEntity(player);


        gameData.setDelta(0.1f);
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);
        gameData.setKeys(new GameKeys());
    }

    @Test
    public void testPlayerMovesForwardWhenUpKeyIsPressed() {

        gameData.getKeys().setKey(GameKeys.UP, true);

        float xBefore = (float) player.getX();
        float yBefore = (float) player.getY();


        playerControlSystem.process(gameData, world);

        float xAfter = (float) player.getX();
        float yAfter = (float) player.getY();

        boolean moved = xAfter != xBefore || yAfter != yBefore;
        assertTrue(moved, "Player should have moved forward when UP key is pressed");
    }
}
