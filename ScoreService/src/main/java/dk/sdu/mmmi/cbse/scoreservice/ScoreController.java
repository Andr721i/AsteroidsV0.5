package dk.sdu.mmmi.cbse.scoreservice;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/score")
public class ScoreController {

    private int currentScore = 0;

    @GetMapping
    public int getScore() {
        return currentScore;
    }

    @PostMapping
    public void updateScore(@RequestBody int score) {
        this.currentScore = score;
    }
}
