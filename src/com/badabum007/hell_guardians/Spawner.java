package com.badabum007.hell_guardians;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class defines enemy spawner
 * 
 * @author badabum007
 */
public class Spawner {
  /** summary spawned creatures number */
  int count;

  int startPosX;
  int startPosY;

  /** unleashed monsters count */
  int iterator;

  /** list of all existing enemies from this spawner */
  ArrayList<Enemy> enemies;

  int X;
  /** enemy step size */
  int enemyStepSize = 1;

  /**
   * create a spawner
   * 
   * @param count - spawning enemies count
   * @param startPosX - start X coordinate
   * @param startPosY - start Y coordinate
   */
  public Spawner(int count, int startPosX, int startPosY) {
    this.startPosX = startPosX;
    this.startPosY = startPosY;
    this.count = count;
    iterator = 0;
    enemies = new ArrayList<Enemy>();
  }

  /**
   * enemy creation
   * 
   * @throws IOException
   */
  public void CreateMonster() throws IOException {
    enemies.add(new Enemy(startPosX, startPosY));
    iterator++;
  }

  /** updates enemy location */
  public int update() {
    for (int j = 0; j < enemies.size(); j++) {
      X = (int) (enemies.get(j).posX);
      if (X > GameWindow.offsetXY) {
        enemies.get(j).moveX(enemyStepSize);
        continue;
      } else {
        return -1;
      }
    }
    return 0;
  }
}
