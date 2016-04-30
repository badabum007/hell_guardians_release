package com.badabum007.hell_guardians;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * class for making animation
 * 
 * @author badabum007
 */
public class SpriteAnimation extends Transition {

  private final ImageView imageView;
  /** count of animation stages */
  private final int count;
  /** number of required sprite columns */
  private final int columns;
  /** sprite offset from the beginning of sprite list */
  private final int offsetX;
  private final int offsetY;
  /** sprite width and height */
  private final int width;
  private final int height;

  private int lastIndex;

  public SpriteAnimation(ImageView imageView,
      /** animation duration */
      Duration duration, int count, int columns, int offsetX, int offsetY, int width, int height) {
    this.imageView = imageView;
    this.count = count;
    this.columns = columns;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.width = width;
    this.height = height;
    setCycleDuration(duration);
    setInterpolator(Interpolator.LINEAR);
  }

  /** finds our sprite position */
  protected void interpolate(double k) {
    final int index = Math.min((int) Math.floor(k * count), count - 1);
    if (index != lastIndex) {
      final int x = (index % columns) * width + offsetX;
      final int y = (index / columns) * height + offsetY;
      imageView.setViewport(new Rectangle2D(x, y, width, height));
      lastIndex = index;
    }
  }
}
