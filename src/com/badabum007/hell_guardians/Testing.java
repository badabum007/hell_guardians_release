package com.badabum007.hell_guardians;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

//class for testing SpriteAnimation
//SpriteAnimation will be used in the next version
public class Testing  {
  public static void show(Stage primaryStage) throws IOException{
    InputStream is = Files.newInputStream(Paths.get("res/images/sarcher_sprites.png"));
    final Image IMAGE = new Image(is);
    is.close();

    final int COLUMNS  =  6;
    final int COUNT    =  6;
    final int OFFSET_X =  0;
    final int OFFSET_Y =  125;
    final int WIDTH    = 90;
    final int HEIGHT   = 125;
    final int DURATION = 1000;

    final ImageView imageView = new ImageView(IMAGE);
    imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));

    final Animation animation = new SpriteAnimation(
        imageView,
        Duration.millis(DURATION),
        COUNT, COLUMNS,
        OFFSET_X, OFFSET_Y,
        WIDTH, HEIGHT
        );
    animation.setCycleCount(Animation.INDEFINITE);
    animation.play();

    //primaryStage.setScene(new Scene(new Group(imageView)));
  }
};