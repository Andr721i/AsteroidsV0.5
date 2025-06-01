import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.playersystem.Player;
import dk.sdu.mmmi.cbse.playersystem.PlayerControlSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

        when(gameData.getKeys()).thenReturn(keys);
        when(gameData.getDelta()).thenReturn(0.1f);

        player = new Player();
        player.setX(100);
        player.setY(100);
        player.setRotation(0);

        when(world.getEntities(Player.class)).thenReturn(Collections.singletonList(player));
    }

    @Test
    public void testPlayerMovesWhenUpKeyPressed() {
        when(keys.isDown(GameKeys.UP)).thenReturn(true);

        float initialX = (float) player.getX();
        float initialY = (float) player.getY();

        playerControlSystem.process(gameData, world);

        float newX = (float) player.getX();
        float newY = (float) player.getY();

        boolean hasMoved = (initialX != newX) || (initialY != newY);


        assertNotEquals(false, hasMoved, "Player should move when UP key is pressed");
    }
}
