package com.badabum007.hell_guardians;

import java.io.IOException;
import java.util.Random;

/**
 * Bot implementation
 * 
 * @author badabum007
 */
public class Bot {
  /** tower coordinates */
  int coordX;
  int coordY;

  /** max tower maxCount */
  int maxCount = GameRoot.rows * GameRoot.columns;
  /** current towers count */
  int currentCount;

  /** tower map */
  boolean[][] towerMap;

  /** bot initialization */
  public Bot() {
    currentCount = 0;
    coordX = 0;
    coordY = 0;
    towerMap = new boolean[GameRoot.rows][GameRoot.columns];
    for (int i = 0; i < GameRoot.rows; i++) {
      for (int j = 0; j < GameRoot.columns; j++) {
        towerMap[i][j] = false;
      }
    }
  }

  /**
   * Tower creation
   * 
   * @throws IOException
   */
  void createTower() throws IOException {
    /** check if the block is free */
    do {
      coordY = (int) (new Random().nextInt(GameRoot.rows));
      coordX = (int) (new Random().nextInt(GameRoot.columns));
    } while (towerMap[coordY][coordX] == true);
    towerMap[coordY][coordX] = true;

    Tower tower = new Tower(coordX * GameWindow.blockSize + GameWindow.offsetXY,
        coordY * GameWindow.blockSize + GameWindow.offsetXY);
    GameWindow.gameRoot.sMan.addToFile(GameWindow.gameRoot.sMan.tempFileName,
        tower.posX + " " + tower.posY + " " + GameWindow.gameRoot.towerTime + "\n");
    GameWindow.gameRoot.towers.add(tower);
    currentCount++;
  }
}
