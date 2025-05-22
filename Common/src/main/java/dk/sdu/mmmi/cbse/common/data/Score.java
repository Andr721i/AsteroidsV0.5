package dk.sdu.mmmi.cbse.common.data;

public class Score {
    private int currentScore;

    public Score() {
        this.currentScore = 0;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void addPoints(int points) {
        this.currentScore += points;
    }

    public void reset() {
        this.currentScore = 0;
    }
}
