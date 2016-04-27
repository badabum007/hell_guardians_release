package com.badabum007.hell_guardians;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Class defines a single enemy
 * 
 * @author badabum007
 */
public class Enemy extends Pane {
  ImageView imageView;

  /** enemy position */
  public double posX;
  public double posY;
  int health = 100;

  final int width = 145;
  final int height = 100;
  final int offsetX = 0;
  final int offsetY = 330;
  final int count = 6;
  final int columns = 6;
  final int duration = 700;
  SpriteAnimation animation;

  /**
   * Creates an enemy at the pointed location
   * 
   * @param posX - start X position
   * @param posY - start Y position
   * @throws IOException
   */
  public Enemy(int posX, int posY) throws IOException {
    /** load enemy sprite and enable enemy animation */
    InputStream is = Files.newInputStream(Paths.get("res/images/hero_sprites.png"));
    Image img = new Image(is);
    is.close();
    this.imageView = new ImageView(img);
    this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    animation = new SpriteAnimation(imageView, Duration.millis(duration), count, columns, offsetX,
        offsetY, width, height);
    animation.setCycleCount(Animation.INDEFINITE);
    animation.play();
    this.posX = posX;
    this.posY = posY;
    this.setTranslateX(posX);
    this.setTranslateY(posY);
    getChildren().add(imageView);
    GameWindow.gameRoot.getChildren().add(this);
  }

  /**
   * Enemy movement control
   * 
   * @param x - number of steps to move (if x < 0 enemy moves from right to left)
   */
  public void moveX(double x) {
    this.setTranslateX(this.getTranslateX() - x);
    posX -= x;
  }

  /**
   * method responds for losing health after getting a damage
   * 
   * @param damage - amount of damage, got from tower
   */
  public void getDamage(int damage) {
    health = health - damage;
    if (health <= 0) {
      this.setVisible(false);
      GameWindow.gameRoot.getChildren().remove(this);
      this.animation.stop();
    }
  }
}
