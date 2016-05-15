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
 * Class defines single tower
 * 
 * @author badabum007
 */
public class Tower extends Pane {
  ImageView imageView;

  /** tower position */
  long posX;
  long posY;
  double attackRange = MainGameMenu.width - GameWindow.offsetXY;
  final double shootingCooldown = 10;

  /** time to next shot */
  double timeToShoot;

  SpriteAnimation animation;

  final int columns = 8;
  final int count = 8;
  final int offsetX = 0;
  final int offsetY = 535;
  final int width = 110;
  final int height = 125;
  final int duration = 1000;

  /**
   * Build a tower
   * 
   * @param x - tower X position
   * @param y - tower Y position
   * @throws IOException
   */
  public Tower(long x, long y) throws IOException {
    timeToShoot = 0;

    InputStream is = Files.newInputStream(Paths.get("res/images/sarcher_sprites.png"));
    Image img = new Image(is);
    is.close();
    this.imageView = new ImageView(img);

    this.posX = x;
    this.posY = y;
    this.setTranslateX(posX);
    this.setTranslateY(posY);
    imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    Animation animation = new SpriteAnimation(imageView, Duration.millis(duration), count, columns,
        offsetX, offsetY, width, height);
    animation.setCycleCount(Animation.INDEFINITE);
    animation.play();
    getChildren().add(imageView);
    GameWindow.gameRoot.getChildren().add(this);
  }
}