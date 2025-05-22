package dk.sdu.mmmi.cbse.common.data;

public class GameState {
    private static boolean isGameOver = false;

    public static boolean isGameOver() {
        return isGameOver;
    }

    public static void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public static void reset() {
        isGameOver = false;
    }
}
