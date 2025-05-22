package dk.sdu.mmmi.cbse.scoreserviceclient;

import org.springframework.web.client.RestTemplate;

public class ScoreClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080/score";

    // Get current score
    public int getScore() {
        return restTemplate.getForObject(baseUrl, Integer.class);
    }

    // Update score
    public void updateScore(int score) {
        restTemplate.postForObject(baseUrl, score, Void.class);
    }
}
