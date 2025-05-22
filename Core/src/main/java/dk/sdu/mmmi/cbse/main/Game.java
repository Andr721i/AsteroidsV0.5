package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import dk.sdu.mmmi.cbse.common.data.GameState;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> shapeMap = new ConcurrentHashMap<>();
    private final Pane root = new Pane();
    private final List<IGamePluginService> pluginList;
    private final List<IEntityProcessingService> processorList;
    private final List<IPostEntityProcessingService> postProcessorList;
    private long previousTime = 0;
    private AnimationTimer gameLoop;

    Game(List<IGamePluginService> pluginList, List<IEntityProcessingService> processorList, List<IPostEntityProcessingService> postProcessorList) {
        this.pluginList = pluginList;
        this.processorList = processorList;
        this.postProcessorList = postProcessorList;
    }

    public void start(Stage stage) throws Exception {
        Text info = new Text(10, 20, "Destroyed asteroids: 0");
        root.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        root.getChildren().add(info);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            KeyCode k = e.getCode();
            if (k == KeyCode.LEFT) gameData.getKeys().setKey(GameKeys.LEFT, true);
            if (k == KeyCode.RIGHT) gameData.getKeys().setKey(GameKeys.RIGHT, true);
            if (k == KeyCode.UP) gameData.getKeys().setKey(GameKeys.UP, true);
            if (k == KeyCode.SPACE) gameData.getKeys().setKey(GameKeys.SPACE, true);
        });

        scene.setOnKeyReleased(e -> {
            KeyCode k = e.getCode();
            if (k == KeyCode.LEFT) gameData.getKeys().setKey(GameKeys.LEFT, false);
            if (k == KeyCode.RIGHT) gameData.getKeys().setKey(GameKeys.RIGHT, false);
            if (k == KeyCode.UP) gameData.getKeys().setKey(GameKeys.UP, false);
            if (k == KeyCode.SPACE) gameData.getKeys().setKey(GameKeys.SPACE, false);
        });

        for (IGamePluginService plugin : pluginList) {
            plugin.start(gameData, world);
        }

        for (Entity ent : world.getEntities()) {
            Polygon shape = new Polygon(ent.getPolygonCoordinates());
            shapeMap.put(ent, shape);
            root.getChildren().add(shape);
        }

        stage.setScene(scene);
        stage.setTitle("ASTEROIDS");
        stage.show();

        render();
    }

    public void render() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                if (GameState.isGameOver()) {
                    showGameOverScreen();
                    stop(); // stop game loop
                    return;
                }

                if (previousTime > 0) {
                    float delta = (currentTime - previousTime) / 1_000_000_000f;
                    gameData.setDelta(delta);
                }
                previousTime = currentTime;

                update();
                draw();
                gameData.getKeys().update();
            }
        };
        gameLoop.start();
    }

    private void update() {
        for (IEntityProcessingService processor : processorList) {
            processor.process(gameData, world);
        }
        for (IPostEntityProcessingService postProcessor : postProcessorList) {
            postProcessor.process(gameData, world);
        }
    }

    private void draw() {
        shapeMap.keySet().removeIf(entity -> {
            if (!world.getEntities().contains(entity)) {
                root.getChildren().remove(shapeMap.get(entity));
                return true;
            }
            return false;
        });

        for (Entity entity : world.getEntities()) {
            Polygon shape = shapeMap.get(entity);
            if (shape == null) {
                shape = new Polygon(entity.getPolygonCoordinates());
                shapeMap.put(entity, shape);
                root.getChildren().add(shape);
            }
            shape.setTranslateX(entity.getX());
            shape.setTranslateY(entity.getY());
            shape.setRotate(entity.getRotation());
        }
    }

    private void showGameOverScreen() {
        Text gameOverText = new Text("Game Over");
        gameOverText.setStyle("-fx-font-size: 48px;");
        gameOverText.setTranslateX(gameData.getDisplayWidth() / 2.0 - 120);
        gameOverText.setTranslateY(gameData.getDisplayHeight() / 2.0 - 50);

        Button respawnButton = new Button("Respawn");
        respawnButton.setTranslateX(gameData.getDisplayWidth() / 2.0 - 40);
        respawnButton.setTranslateY(gameData.getDisplayHeight() / 2.0);

        respawnButton.setOnAction(e -> {
            GameState.reset();
            root.getChildren().removeAll(gameOverText, respawnButton);
            respawnPlayer();
            render(); // restart game loop
        });

        root.getChildren().addAll(gameOverText, respawnButton);
    }

    private void respawnPlayer() {
        world.clear();          // Clear world entities
        shapeMap.clear();       // Clear shape mappings
        root.getChildren().clear(); // Remove all graphics

        // Optionally re-add static UI elements here, like score display

        for (IGamePluginService plugin : pluginList) {
            plugin.start(gameData, world); // Respawn player and game entities
        }

        for (Entity ent : world.getEntities()) {
            Polygon shape = new Polygon(ent.getPolygonCoordinates());
            shapeMap.put(ent, shape);
            root.getChildren().add(shape);
        }
    }

    public List<IGamePluginService> getGamePluginServices() {
        return pluginList;
    }

    public List<IEntityProcessingService> getEntityProcessingServices() {
        return processorList;
    }

    public List<IPostEntityProcessingService> getPostEntityProcessingServices() {
        return postProcessorList;
    }
}
