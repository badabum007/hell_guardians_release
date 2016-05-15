package com.badabum007.hell_guardians;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/**
 * Creates a block for tower building and controls block behavior
 * 
 * @author badabum007
 */
public class Block extends Pane {
  ImageView block;
  static Image img_block;
  static InputStream is;

  /**
   * Creates a block in a specified point
   * 
   * @param x - X coordinate
   * @param y - Y coordinate
   * @throws IOException
   */
  public Block(int x, int y) throws IOException {
    block = new ImageView();
    block.setFitHeight(GameWindow.blockSize);
    block.setFitWidth(GameWindow.blockSize);
    setTranslateX(x);
    setTranslateY(y);
    block.setImage(img_block);

    Line line1 = new Line(x, y, x + GameWindow.blockSize, y);
    Line line2 =
        new Line(x + GameWindow.blockSize, y, x + GameWindow.blockSize, y + GameWindow.blockSize);
    Line line3 =
        new Line(x + GameWindow.blockSize, y + GameWindow.blockSize, x, y + GameWindow.blockSize);
    Line line4 = new Line(x, y + GameWindow.blockSize, x, y);
    /** Indent block on mouse entering */
    this.setOnMouseEntered(event -> {
      GameWindow.gameRoot.getChildren().addAll(line1, line2, line3, line4);
    });
    /** Remove block indenting */
    this.setOnMouseExited(event -> {
      GameWindow.gameRoot.getChildren().removeAll(line1, line2, line3, line4);
    });
    /** Build a tower on mouse click */
    if (GameRoot.gameMode == "Normal") {
      this.setOnMouseClicked(event -> {
        Tower tower;
        try {
          GameWindow.gameRoot.sMan.addToFile(GameWindow.gameRoot.sMan.tempFileName,
              x + " " + y + " " + GameWindow.gameRoot.towerTime + "\n");
          tower = new Tower(x, y);
          GameWindow.gameRoot.towers.add(tower);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
    /**
     * Add our block to the gameRoot
     */
    getChildren().add(block);
    GameWindow.gameRoot.getChildren().add(this);
  }
  
  public static void init(){
    /** block image adding */
    try {
      is = Files.newInputStream(Paths.get("res/images/block.jpg"));
      img_block = new Image(is);
      is.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
