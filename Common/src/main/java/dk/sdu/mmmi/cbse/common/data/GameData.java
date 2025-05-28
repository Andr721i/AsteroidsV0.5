package dk.sdu.mmmi.cbse.common.data;

import java.util.ArrayList;
import java.util.List;

public class GameData {

    private int displayWidth  = 800 ;
    private int displayHeight = 800;
    private final GameKeys keys = new GameKeys();
    private float delta;

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;


    }
    private Score score = new Score();

    public Score getScore() {
        return score;
    }


    public GameKeys getKeys() {
        return keys;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    private List<String> scoreEvents = new ArrayList<>();

    public void addScoreEvent(String event) {
        scoreEvents.add(event);
    }

    public List<String> getScoreEvents() {
        return scoreEvents;
    }

    public void clearScoreEvents() {
        scoreEvents.clear();
    }

}
