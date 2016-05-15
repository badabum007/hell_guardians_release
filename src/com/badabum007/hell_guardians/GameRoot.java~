package com.badabum007.hell_guardians;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * game engine class
 * 
 * @author badabum007
 */
public class GameRoot extends Pane implements Runnable {

  public static final Object monitor = new Object();
  Thread thread;

  public Money money;
  public Integer yourMoney = 0;
  final public Integer enemyCost = 100;

  /** existing towers */
  ArrayList<Tower> towers;
  ArrayList<Shot> shots;

  /** game mode: Auto(botplay) or Normal */
  public static String gameMode;
  public static String difficulty;

  /** bot for Auto mode */
  Bot bot;

  private MediaPlayer menuMp;

  public static final int rows = 4;
  public static final int columns = 6;

  /** timer update frequency */
  final int updateFrequency = 10000000;

  /** existing enemy spawners */
  Spawner[] spawn;

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
  long[][] argsFromFile;

  /** number of strings in file */
  int maxStringCount = 0;

  SaveManager sMan;
  String tempFileName = "saves/logs.txt";

  int argsCount = 3;
  int counter;

  int xArg = 0;
  int yArg = 1;
  int timeArg = 2;

  int botsPerWaveInc = 1;
  long sleepTime = 5000;
  long tickPerSec = 55;
  long exitTimerLimit = tickPerSec * 5;

  int damageHorror = 25;
  int damageNightmare = 20;

  Iterator<Shot> iter;

  Shot tempShot;

  @Override
  public void run() {
    UpdateMoney();
  }

  public void Start() {
    thread.start();
  }

  /** operations with money */
  class Money extends Parent {

    // make values as Integers to convert them into String later
    private Integer money;
    Text m;
    // create new Pane to overlap the background
    private Pane pm;

    public Money() throws IOException {
      pm = new Pane();
      int imgWidth = 50;

      /** setting money picture */
      int moneyTransX = 620;
      InputStream is = Files.newInputStream(Paths.get("res/images/souls.png"));
      Image img = new Image(is);
      ImageView imgView = new ImageView(img);
      imgView.setFitWidth(imgWidth);
      imgView.setPreserveRatio(true);
      imgView.setTranslateX(moneyTransX);

      is.close();
      money = 0;

      /** setting money text */
      int fontSize = 20, textTransY = 70;

      m = new Text(moneyTransX, textTransY, money.toString());
      m.setFont(new Font(fontSize));
      m.setFill(Color.GREY);

      pm.getChildren().addAll(imgView, m);
      getChildren().add(pm);
    }

    /** methods to change value of money and scores */
    public void setMoney(Integer x) {
      m.setText(x.toString());
    }
  }

  public GameRoot() {
    towers = new ArrayList<Tower>();
    shots = new ArrayList<Shot>();
    spawn = new Spawner[rows];
    this.setVisible(false);
    bot = new Bot();
    towerTime = 0;
    sMan = new SaveManager(tempFileName);
    thread = new Thread(this);

    Media media = new Media(
        new File("res/music/Gonzalo_Varela_-_03_-_Underwater_Lab.mp3").toURI().toString());
    menuMp = new MediaPlayer(media);
    /** song autostart after adding */
    /** play song in infinity loop */
    menuMp.setCycleCount(MediaPlayer.INDEFINITE);
    MediaView mediaView = new MediaView(menuMp);
    getChildren().add(mediaView);
    sMan.createTempFile(tempFileName);

    try {
      Shot.init();
      Enemy.init();
      Block.init();
      money = new Money();
      getChildren().add(money);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * game logic implementation
   */
  public void StartGame() throws IOException {

    if (difficulty == "Horror") {
      Shot.damage = damageHorror;
    }
    if (difficulty == "Nightmare") {
      Shot.damage = damageNightmare;
    }
    menuMp.play();
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
      spawn[i] = new Spawner(enemyCount, MainGameMenu.width,
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
              spawn[i].count += enemyCount;
            }
            waveTick = 0;
          }

          for (int i = 0; i < rows; i++) {
            /** generate new mob */
            if (spawn[i].iterator < spawn[i].count) {
              try {
                spawn[i].CreateMonster();
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
          /** reduce cooldown */
          CheckForShooting();
          for (int i = 0; i < towers.size(); i++) {
            towers.get(i).timeToShoot -= shootTimeStep;
          }
        }
        /** update enemy position */
        if (now / updateFrequency != frameTimer.get()) {
          for (int i = 0; i < rows; i++) {
            if (spawn[i].update() < 0) {
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

          iter = shots.iterator();
          while (iter.hasNext()) {
            tempShot = (Shot) iter.next();
            if (tempShot.update() < 0) {
              iter.remove();
            }
          }

        }
        frameTimer.set(now / updateFrequency);
        checkForShootTimer.set(now / updateFrequency);
      }
    };
    timer.start();
    Start();
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

  public void UpdateMoney() {

    while (true) {
      synchronized (monitor) {
        try {
          monitor.wait();
          yourMoney += enemyCost;
          money.setMoney(yourMoney);
        } catch (InterruptedException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    }
  }

  /** find target and generate a shot */
  public void CheckForShooting() {

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < spawn[i].enemies.size(); j++) {
        if (spawn[i].enemies.get(j).health <= 0) {

          synchronized (monitor) {
            monitor.notify();
            spawn[i].enemies.remove(j);
            continue;
          }
        }
        for (int k = 0; k < towers.size(); k++) {
          double EnemyPosX = spawn[i].enemies.get(j).getTranslateX();
          double EnemyPosY = spawn[i].enemies.get(j).getTranslateY();
          double TowerPosX = towers.get(k).getTranslateX();
          double TowerPosY = towers.get(k).getTranslateY();
          /** enemy is in a towers line in front of the tower */
          if ((EnemyPosX - TowerPosX > 0) && (TowerPosY - EnemyPosY == 0)
              && (EnemyPosX < MainGameMenu.width - GameWindow.offsetXY)) {
            /** cooldown checking */
            if (towers.get(k).timeToShoot <= 0) {
              towers.get(k).timeToShoot = towers.get(k).shootingCooldown;
              try {
                shots.add(
                    new Shot(spawn[i].enemies.get(j), towers.get(k).posX + GameWindow.blockSize / 2,
                        towers.get(k).posY + GameWindow.blockSize / 2));
              } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            }
          }
        }
      }
    }
  }
}
