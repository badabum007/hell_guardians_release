package com.badabum007.hell_guardians;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 * Tower shot class
 * 
 * @author badabum007
 */
public class Shot extends Circle {

  Enemy target;
  public static int damage;

  /** start coordinates */
  double startX;
  double startY;

  double radius = 5;
  double duration = 200;

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
  public Shot(Enemy target, double startX, double startY) {
    this.target = target;
    this.startX = startX;
    this.startY = startY;
    this.setCenterX(startX);
    this.setCenterY(startY);
    this.setRadius(radius);
    GameWindow.gameRoot.getChildren().add(this);
    /** define where and from to shoot */
    shotPath = new Path(new MoveTo(startX, startY));
    shotPath.getElements()
        .add(new LineTo(target.posX + target.width / 2, target.posY + target.height / 2));
    animation = new PathTransition(Duration.millis(duration), shotPath, this);
    animation.play();
    /** on shot end */
    animation.setOnFinished(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        PathTransition finishedAnimation = (PathTransition) actionEvent.getSource();
        Shot finishedShot = (Shot) finishedAnimation.getNode();
        finishedShot.setVisible(false);
        target.getDamage(damage);
        GameWindow.gameRoot.getChildren().remove(finishedShot);
      }
    });
  }
}
