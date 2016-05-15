package com.badabum007.hell_guardians;

import java.io.IOException;

public class Shooter implements Runnable {
  Thread thread;

  @Override
  public void run() {
    CheckForShooting();
  }
  
  public Shooter(){
  thread = new Thread(this);
  }
  
  public void Start() {
    thread.start();
  }
  
  /** find target and generate a shot */
  public void CheckForShooting() {

    synchronized (GameRoot.monitor) {
      try {
        GameRoot.monitor.wait();
      } catch (InterruptedException e1) {

        // TODO Auto-generated catch block
        // e1.printStackTrace();
        for (int i = 0; i < GameRoot.rows; i++) {
          for (int j = 0; j < GameWindow.gameRoot.spawn[i].enemies.size(); j++) {
            if (GameWindow.gameRoot.spawn[i].enemies.get(j).health <= 0) {
              GameWindow.gameRoot.spawn[i].enemies.remove(j);
              continue;
            }
            for (int k = 0; k < GameWindow.gameRoot.towers.size(); k++) {
              double EnemyPosX = GameWindow.gameRoot.spawn[i].enemies.get(j).getTranslateX();
              double EnemyPosY = GameWindow.gameRoot.spawn[i].enemies.get(j).getTranslateY();
              double TowerPosX = GameWindow.gameRoot.towers.get(k).getTranslateX();
              double TowerPosY = GameWindow.gameRoot.towers.get(k).getTranslateY();
              /** enemy is in a towers line in front of the tower */
              if ((EnemyPosX - TowerPosX > 0) && (TowerPosY - EnemyPosY == 0)
                  && (EnemyPosX < MainGameMenu.width - GameWindow.offsetXY)) {
                /** cooldown checking */
                if (GameWindow.gameRoot.towers.get(k).timeToShoot <= 0) {
                  GameWindow.gameRoot.towers.get(k).timeToShoot =
                      GameWindow.gameRoot.towers.get(k).shootingCooldown;
                  try {
                    GameWindow.gameRoot.shots
                        .add(new Shot(GameWindow.gameRoot.spawn[i].enemies.get(j),
                            GameWindow.gameRoot.towers.get(k).posX + GameWindow.blockSize / 2,
                            GameWindow.gameRoot.towers.get(k).posY + GameWindow.blockSize / 2));
                  } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                  }
                }
              }
            }
          }
        }


        GameRoot.monitor.notify();
      }
    }
  }

}
