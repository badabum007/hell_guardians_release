package com.badabum007.hell_guardians;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * game engine class
 * 
 * @author badabum007
 */
public class GameRoot extends Pane {

  /** existing towers */
  ArrayList<Tower> towers;

  /** game mode: Auto(botplay) or Normal */
  public static String gameMode;

  /** bot for Auto mode */
  Bot bot;

  private MediaPlayer menuMp;

  public static final int rows = 4;
  public static final int columns = 6;

  /** timer update frequency */
  final int updateFrequency = 10000000;

  /** existing enemy spawners */
  Spawner[] Spawn;
  
  double shootTimeStep = 0.1;

  int enemyCount = 2;
  long timeToNextWave = 500;
  final long timeToNextMob = 100;

  LongProperty checkForShootTimer;
  LongProperty frameTimer;

  long frameTimerInit = 0;
  
  int randChance = 3;
  int botFastestTime = 50;
  int botRandomPart = 70;

  public GameRoot() {
    towers = new ArrayList<Tower>();
    Spawn = new Spawner[rows];
    this.setVisible(false);
    bot = new Bot();
  }

  /**
   * game logic implementation
   */
  public void StartGame() throws IOException {
    Media media = new Media(
        new File("res/music/Gonzalo_Varela_-_03_-_Underwater_Lab.mp3").toURI().toString());
    menuMp = new MediaPlayer(media);
    /** song autostart after adding */
    menuMp.setAutoPlay(true);
    /** play song in infinity loop */
    menuMp.setCycleCount(MediaPlayer.INDEFINITE);
    menuMp.play();
    MediaView mediaView = new MediaView(menuMp);
    getChildren().add(mediaView);
    CreateMap();
    for (int i = 0; i < rows; i++) {
      Spawn[i] = new Spawner(enemyCount, MainGameMenu.width,
          GameWindow.offsetXY + i * GameWindow.blockSize);
    }
    /** timer description */
    checkForShootTimer = new SimpleLongProperty();
    frameTimer = new SimpleLongProperty(frameTimerInit);
    AnimationTimer timer = new AnimationTimer() {
      /** timer counters */
      long everyTick = 0;
      long waveTick = 0;
      long everyTickForBot = 0;

      @Override
      public void handle(long now) {
        everyTick++;
        waveTick++;
        if (everyTick > timeToNextMob) {
          everyTick = 0;
          /** generate new wave */
          if (waveTick > (int) (new Random().nextInt((int) timeToNextWave))) {
            timeToNextWave += timeToNextMob;
            enemyCount += 1;
            for (int i = 0; i < rows; i++) {
              if ((int) (new Random().nextInt(randChance)) == 0) {
                Spawn[i].count += (int) (new Random().nextInt((int) enemyCount));
              }
            }
            waveTick = 0;
          }

          for (int i = 0; i < rows; i++) {
            /** generate new mob */
            if (Spawn[i].iterator < Spawn[i].count) {
              try {
                if ((int) (new Random().nextInt(randChance)) == 0) {
                  Spawn[i].CreateMonster();
                }
                /** update enemies position */
                if (now / updateFrequency != frameTimer.get()) {
                  if (Spawn[i].update() < 0) {
                    InputStream is;
                    try {
                      is = Files.newInputStream(Paths.get("res/images/game_over.jpg"));
                      Image img = new Image(is);
                      ImageView imgView = new ImageView(img);
                      getChildren().add(imgView);
                      this.stop();
                      is.close();
                    } catch (IOException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                    }
                  } ;
                }
                frameTimer.set(now / updateFrequency);
              } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            }
          }
        }

        /** bot builds tower */
        if (gameMode == "Auto") {
          everyTickForBot++;
          if (everyTickForBot > (int) (Math.random() * botRandomPart + botFastestTime)) {
            everyTickForBot = 0;
            if (bot.currentCount < bot.maxCount) {
              try {
                bot.createTower();
              } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            }
          }
        }

        /** check if the tower is ready for shot */
        if (now / updateFrequency != checkForShootTimer.get()) {
          CheckForShooting();
          /** reduce cooldown */
          for (int i = 0; i < towers.size(); i++) {
            towers.get(i).timeToShoot -= shootTimeStep;
          }
        }
        /** update enemy position */
        if (now / updateFrequency != frameTimer.get()) {
          for (int i = 0; i < rows; i++) {
            if (Spawn[i].update() < 0) {
              InputStream is;
              try {
                is = Files.newInputStream(Paths.get("res/images/game_over.jpg"));
                Image img = new Image(is);
                ImageView imgView = new ImageView(img);
                getChildren().add(imgView);
                this.stop();
                is.close();
              } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            } ;
          }
        }
        frameTimer.set(now / updateFrequency);
        checkForShootTimer.set(now / updateFrequency);
      }
    };
    timer.start();
  }

  /**
   * map generation
   * 
   * @throws IOException
   */

  public void CreateMap() throws IOException {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        Block bl = new Block(GameWindow.offsetXY + j * GameWindow.blockSize,
            GameWindow.offsetXY + i * GameWindow.blockSize);
      }
    }
  }

  /** find target and generate a shot */
  public void CheckForShooting() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < Spawn[i].enemies.size(); j++) {
        if (Spawn[i].enemies.get(j).health <= 0) {
          Spawn[i].enemies.remove(j);
          continue;
        }
        for (int k = 0; k < towers.size(); k++) {
          double EnemyPosX = Spawn[i].enemies.get(j).getTranslateX();
          double EnemyPosY = Spawn[i].enemies.get(j).getTranslateY();
          double TowerPosX = towers.get(k).getTranslateX();
          double TowerPosY = towers.get(k).getTranslateY();
          /** enemy is in a towers line in front of the tower */
          if ((EnemyPosX - TowerPosX > 0) && (TowerPosY - EnemyPosY == 0)
              && (EnemyPosX < MainGameMenu.width - GameWindow.offsetXY)) {
            /** cooldown checking */
            if (towers.get(k).timeToShoot <= 0) {
              towers.get(k).timeToShoot = towers.get(k).shootingCooldown;
              towers.get(k).shots =
                  new Shot(Spawn[i].enemies.get(j), towers.get(k).posX + GameWindow.blockSize / 2,
                      towers.get(k).posY + GameWindow.blockSize / 2);
            }
          }
        }
      }
    }
  }
}
