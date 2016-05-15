package com.badabum007.hell_guardians;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Class for basic game screen setting
 * 
 * @author badabum007
 */
public class GameWindow {
  /** size of one block */
  public static final int blockSize = 100;
  /** block field XY offset from the top left screen corner */
  public static final int offsetXY = 100;

  static GameRoot gameRoot = new GameRoot();
  private Scene gameScene;
  static Pane All;

  int height = 600, width = 800;


  /**
   * Setting up game scene
   * 
   * @throws IOException
   */
  public void show(Stage primaryStage) throws IOException {
    Pane All = new Pane();
    gameScene = new Scene(All);

    /** add awesome lava background */
    All.setPrefSize(width, height);
    InputStream is = Files.newInputStream(Paths.get("res/images/texture.jpg"));
    Image img = new Image(is);
    ImageView imgView = new ImageView(img);
    imgView.setFitWidth(width);
    imgView.setFitHeight(height);
    is.close();

    All.getChildren().addAll(imgView, gameRoot);
    primaryStage.setScene(gameScene);

    gameRoot.setVisible(true);
    gameRoot.StartGame();
    //CheckForShooting();
  }
  
}
