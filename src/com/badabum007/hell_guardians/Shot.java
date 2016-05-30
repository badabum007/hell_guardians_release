package com.badabum007.hell_guardians;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.animation.PathTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Path;

/**
 * Tower shot class
 * 
 * @author badabum007
 */
public class Shot {

  ImageView imageView;
  static InputStream is;
  static Image img;

  Enemy target;
  public static int damage;

  /** start coordinates */
  double startX;
  double startY;

  double radius = 5;
  double duration = 200;

  final int offsetX = 825;
  final int offsetY = 200;
  final int width = 25;
  final int height = 70;
  final double rotationDegree = -90;
  final int updateTimer = 1;
  final int stepSize = 5;
  Enemy targetEnemy;

  Spawner spawner;
  /** shot path */
  Path shotPath;
  PathTransition animation;

  /**
   * generate a shot (circle)
   * 
   * @param target - shot target
   * @param startX - start X coordinate
   * @param startY - start Y coordinate
   */
  public Shot(Spawner spawn, double startX, double startY) throws IOException {
    spawner = spawn;
    targetEnemy = spawn.enemies.get(0);
    imageView = new ImageView(img);
    imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    imageView.setRotate(rotationDegree);
    imageView.setTranslateX(startX);
    imageView.setTranslateY(startY - GameWindow.blockSize / 2);
    GameWindow.gameRoot.getChildren().add(imageView);

    targetEnemy = target;
  }

  public int update() {
    if (spawner.enemies.isEmpty() ){
      imageView.setVisible(false);
      GameWindow.gameRoot.getChildren().remove(imageView);
      GameWindow.gameRoot.getChildren().remove(this);
      return -1;
    }
    targetEnemy = spawner.enemies.get(0);
    imageView.setTranslateX(imageView.getTranslateX() + stepSize);
    if (imageView.getTranslateX() > targetEnemy.posX) {
      targetEnemy.getDamage(damage);
      imageView.setVisible(false);
      GameWindow.gameRoot.getChildren().remove(imageView);
      GameWindow.gameRoot.getChildren().remove(this);
      
      return -1;
    }
    return 0;
  }
  
  public static void init() throws IOException{
    is = Files.newInputStream(Paths.get("res/images/sarcher_sprites.png"));
    img = new Image(is);
    is.close();
  }
}