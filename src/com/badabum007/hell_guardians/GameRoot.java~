package com.badabum007.hell_guardians;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

  int enemyCount = 1;
  long timeToNextWave = 500;
  final long timeToNextMob = 100;

  LongProperty checkForShootTimer;
  LongProperty frameTimer;

  long frameTimerInit = 0;

  int botFastestTime = 50;
  int botRandomPart = 500;

  /** RePlay file */
  File saveFile;

  /** tower built time */
  long towerTime;

  /** save arguments from file */
  long argsFromFile[][];

  /** number of strings in file */
  int maxStringCount = 0;

  SaveManager sMan;
  String tempFileName = "saves/positions.txt";

  int argsCount = 3;
  int counter;

  int xArg = 0;
  int yArg = 1;
  int timeArg = 2;

  int botsPerWaveInc = 1;
  long sleepTime = 5000;
  long tickPerSec = 55;
  long exitTimerLimit = tickPerSec * 5;

  public GameRoot() {
    towers = new ArrayList<Tower>();
    Spawn = new Spawner[rows];
    this.setVisible(false);
    bot = new Bot();
    towerTime = 0;
    sMan = new SaveManager(tempFileName);
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

    sMan.createTempFile(tempFileName);
    CreateMap();

    /** Reading args from file and writing the in to array */
    if (gameMode == "RePlay") {
      counter = 0;
      try {
        String[] args = new String[argsCount];
        BufferedReader reader = new BufferedReader(new FileReader(SaveManager.loadGameSave));
        String line;
        maxStringCount = 0;
        Shot.damage = Integer.parseInt(reader.readLine());
        /** Counting string count for array memory allocation */
        while ((line = reader.readLine()) != null) {
          maxStringCount++;
        }
        reader.close();

        reader = new BufferedReader(new FileReader(SaveManager.loadGameSave));
        argsFromFile = new long[maxStringCount][argsCount];
        reader.readLine();
        while ((line = reader.readLine()) != null) {
          args = line.split(" ");
          for (int i = 0; i < argsCount; i++) {
            argsFromFile[counter][i] = Integer.parseInt(args[i]);
          }
          counter++;
        }
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

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
      /** counter for reading args from file */
      int argsCounter = 0;

      @Override
      public void handle(long now) {
        towerTime++;
        everyTick++;
        waveTick++;
        if (everyTick > timeToNextMob) {
          everyTick = 0;
          /** generate new wave */
          if (waveTick > timeToNextWave) {
            timeToNextWave += timeToNextMob;
            enemyCount += botsPerWaveInc;
            Enemy.healthMax += Enemy.healthMax * 0.2;
            for (int i = 0; i < rows; i++) {
              Spawn[i].count += enemyCount;
            }
            waveTick = 0;
          }

          for (int i = 0; i < rows; i++) {
            /** generate new mob */
            if (Spawn[i].iterator < Spawn[i].count) {
              try {
                Spawn[i].CreateMonster();
              } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            }
          }
        }

        /** Setting towers according to built time */
        if (gameMode == "RePlay") {
          if (argsCounter < maxStringCount) {
            if (towerTime > argsFromFile[argsCounter][timeArg]) {
              Tower tower;
              try {
                tower = new Tower(argsFromFile[argsCounter][xArg], argsFromFile[argsCounter][yArg]);
                towers.add(tower);
                argsCounter++;
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
                if (gameMode != "RePlay") {
                  sMan.createSaveFile();
                }
                AnimationTimer exitTimer = new AnimationTimer() {
                  long exitClock = 0;

                  @Override
                  public void handle(long now2) {
                    exitClock++;
                    if (exitClock > exitTimerLimit) {
                      this.stop();
                      java.lang.System.exit(0);
                    }
                  }
                };
                exitTimer.start();
                break;
              } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            }
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
