/*import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.playersystem.Player;
import dk.sdu.mmmi.cbse.playersystem.PlayerControlSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerControlSystemTest {

    private PlayerControlSystem playerControlSystem;
    private GameData gameData;
    private World world;
    private Player player;
    private GameKeys keys;

    @BeforeEach
    public void setup() {
        playerControlSystem = new PlayerControlSystem();

        gameData = mock(GameData.class);
        world = mock(World.class);
        keys = mock(GameKeys.class);

        // Setup gameData.getKeys() to return the mocked keys
        when(gameData.getKeys()).thenReturn(keys);

        // Set delta time (e.g., 0.1 seconds)
        when(gameData.getDelta()).thenReturn(0.1f);

        // Create player entity with initial position and rotation (e.g., rotation 0 degrees)
        player = new Player();
        player.setX(100);
        player.setY(100);
        player.setRotation(0);

        when(world.getEntities(Player.class)).thenReturn(Collections.singletonList(player));
    }

    @Test
    public void testPlayerMovesForwardWhenUpKeyPressed() {
        // Simulate UP key pressed
        when(keys.isDown(GameKeys.UP)).thenReturn(true);

        float initialX = (float) player.getX();
        float initialY = (float) player.getY();
        float initialRotation = (float) player.getRotation();

        System.out.println("Before move: X=" + initialX + ", Y=" + initialY + ", rotation=" + initialRotation);

        // Process player control system logic
        playerControlSystem.process(gameData, world);

        float afterX = (float) player.getX();
        float afterY = (float) player.getY();
        float afterRotation = (float) player.getRotation();

        System.out.println("After move: X=" + afterX + ", Y=" + afterY + ", rotation=" + afterRotation);

        // Assert player moved correctly in the x direction and unchanged y
        assertTrue(afterX > initialX, "Player should move forward on the x axis");
        assertTrue(Math.abs(afterY - initialY) < 0.0001, "Player y position should remain the same");
    }
}

 */
