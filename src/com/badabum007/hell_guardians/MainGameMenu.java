package com.badabum007.hell_guardians;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class is representing title screen menu
 * 
 * @author badabum007
 */
public class MainGameMenu extends Application {
  SaveManager saveMan = new SaveManager();

  public Thread mainThread;
  /** define the stage as public for communication between interface classes */
  public Stage theStage;
  /** create object for our buttons */
  private GameMenu gameMenu;
  /** layout for our title screen menu */
  public static Pane root;
  /** object of our class for all gaming stuff */
  public GameWindow gameWindow;
  /** music player object */
  private MediaPlayer menuMp;
  /** our stage parameters */
  public final static int height = 600, width = 800;
  
  Statistic stat = new Statistic();

  @Override
  public void start(Stage primaryStage) throws Exception {

    //generate saves
    //saveMan.generateSaves(500);

    mainThread = Thread.currentThread();
    root = new Pane();
    gameWindow = new GameWindow();
    theStage = new Stage();
    theStage = primaryStage;

    /** menu content adding */

    /** adding music */
    Media media = new Media(
        new File("res/music/Gonzalo_Varela_-_06_-_Abandoned_Souls.mp3").toURI().toString());
    menuMp = new MediaPlayer(media);
    /** autostart when program is launched */
    menuMp.setAutoPlay(true);
    /** infinite song playing */
    menuMp.setCycleCount(MediaPlayer.INDEFINITE);
    menuMp.play();
    MediaView mediaView = new MediaView(menuMp);

    /** window size */
    root.setPrefSize(width, height);

    /** setting background */
    InputStream is = Files.newInputStream(Paths.get("res/images/main_menu.png"));
    Image img = new Image(is);
    ImageView imgView = new ImageView(img);
    imgView.setFitWidth(width);
    imgView.setFitHeight(height);

    /** disable window resize */
    theStage.setMinWidth(width);
    theStage.setMinHeight(height);
    theStage.setMaxWidth(width);
    theStage.setMaxHeight(height);

    /** initializing our menu */
    gameMenu = new GameMenu();
    gameMenu.setVisible(true);

    root.getChildren().addAll(mediaView, imgView, gameMenu);

    Scene scene = new Scene(root);
    theStage.setTitle("Hell Guardians");
    theStage.setScene(scene);
    theStage.getIcons().add(new Image("file:res/images/icon.png"));

    is.close();
    theStage.show();
  }

  /**
   * Title menu buttons container
   * 
   * @author badabum007
   */
  private class GameMenu extends Parent {

    VBox menu0;
    VBox menu1;
    VBox menu2;

    final int distBetweenButtons = 10;
    /** setting menu position */
    final int menuTransX = 550, menuTransY = 280;
    /** offset for shuffling when menu is changed */
    final int offset = 800;
    double TransTtDur = 0.25;
    double TransTt1Dur = 0.5;


    /**
     * Adds all necessary buttons and sets their behavior
     * 
     * @throws FileNotFoundException
     */
    public GameMenu() throws FileNotFoundException {
      menu0 = new VBox(distBetweenButtons);
      menu1 = new VBox(distBetweenButtons);
      menu2 = new VBox(distBetweenButtons);

      menu0.setTranslateX(menuTransX);
      menu0.setTranslateY(menuTransY);

      menu1.setTranslateX(menuTransX);
      menu1.setTranslateY(menuTransY);

      menu2.setTranslateX(menuTransX);
      menu2.setTranslateY(menuTransY);

      MenuButton btnNewGame = new MenuButton("New game");
      btnNewGame.setOnMouseClicked(event -> {
        getChildren().add(menu1);

        TranslateTransition translateTransition =
            new TranslateTransition(Duration.seconds(TransTtDur), menu0);
        translateTransition.setToX(menu0.getTranslateX() - offset);
        TranslateTransition translateTransition1 =
            new TranslateTransition(Duration.seconds(TransTt1Dur), menu1);
        translateTransition1.setToX(menu0.getTranslateX());

        translateTransition.play();
        translateTransition1.play();

        translateTransition.setOnFinished(evt -> {
          getChildren().remove(menu0);
        });
      });

      MenuButton btnRePlay = new MenuButton("Watch replay");
      btnRePlay.setOnMouseClicked(event -> {
        try {

          /** file choose dialog */
          FileFilter filter = new FileNameExtensionFilter("Hell guardians saves", "sav");
          JFileChooser dialog = new JFileChooser(new File("saves"));
          dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
          dialog.setApproveButtonText("Open");
          dialog.setDialogTitle("Open save");
          dialog.setDialogType(JFileChooser.OPEN_DIALOG);
          dialog.setFileFilter(filter);
          dialog.setMultiSelectionEnabled(false);

          if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            SaveManager.loadGameSave = SaveManager.saveDir + dialog.getSelectedFile().getName();
          } else {
            return;
          }
          GameRoot.gameMode = "RePlay";
          /** stop playing and change scene */
          menuMp.stop();
          gameWindow.show(theStage);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      
      MenuButton btnReadNotation = new MenuButton("Read Notation");
      btnReadNotation.setOnMouseClicked(event -> {
        try {

          /** file choose dialog */
          FileFilter filter = new FileNameExtensionFilter("Hell guardians saves", "sav");
          JFileChooser dialog = new JFileChooser(new File("saves"));
          dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
          dialog.setApproveButtonText("Open");
          dialog.setDialogTitle("Open save");
          dialog.setDialogType(JFileChooser.OPEN_DIALOG);
          dialog.setFileFilter(filter);
          dialog.setMultiSelectionEnabled(false);

          if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            SaveManager.loadGameSave = SaveManager.saveDir + dialog.getSelectedFile().getName();
            SaveManager.showSaveNotation();
            
          } else {
            return;
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      int listViewTransX = 100;
      int listViewTransY = 20;
      int sortIterations = 10;

      MenuButton btnJSort = new MenuButton( "Java Sort");
      btnJSort.setOnMouseClicked(event -> {
        try {
          ListView<String> listView = new ListView<String>();
          long timeJava = System.currentTimeMillis();
          listView.getItems().clear();
          File[] sortedFiles = GameWindow.gameRoot.sMan.getSortedJavaList();
          for (int i = 0; i < sortIterations; i++) {
            sortedFiles = GameWindow.gameRoot.sMan.getSortedJavaList();
          }
          for (int i = 0; i < sortedFiles.length; i++) {
            listView.getItems().add(sortedFiles[i].getName());
          }
          timeJava = System.currentTimeMillis() - timeJava;
          listView.setTranslateX(listViewTransX);
          listView.setTranslateY(listViewTransY);
          getChildren().add(listView);
          System.out.println("Java: " + timeJava);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      
      MenuButton btnSSort = new MenuButton( "Scala Sort");
      btnSSort.setOnMouseClicked(event -> {
        try {
          ListView<String> listView = new ListView<String>();
          long timeScala = System.currentTimeMillis();
          listView.getItems().clear();
          File[] sortedFiles = GameWindow.gameRoot.sMan.getSortedScalaList();
          for (int i = 0; i < sortIterations; i++) {
            sortedFiles = GameWindow.gameRoot.sMan.getSortedScalaList();
          }
          for (int i = 0; i < sortedFiles.length; i++) {
            listView.getItems().add(sortedFiles[i].getName());
          }
          timeScala = System.currentTimeMillis() - timeScala;
          listView.setTranslateX(listViewTransX);
          listView.setTranslateY(listViewTransY);
          getChildren().add(listView);
          System.out.println("Scala: " + timeScala);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      
      MenuButton btnStats = new MenuButton( "Statistics");
      btnStats.setOnMouseClicked(event -> {
        try {
          stat.showStatistic();
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      MenuButton btnBack = new MenuButton("Back");
      btnBack.setOnMouseClicked(event -> {
        getChildren().add(menu0);

        TranslateTransition translateTransition =
            new TranslateTransition(Duration.seconds(TransTtDur), menu1);
        translateTransition.setToX(menu1.getTranslateX() + offset);

        TranslateTransition translateTransition1 =
            new TranslateTransition(Duration.seconds(TransTt1Dur), menu0);
        translateTransition1.setToX(menu1.getTranslateX());

        translateTransition.play();
        translateTransition1.play();

        translateTransition.setOnFinished(evt -> {
          getChildren().remove(menu1);
        });
      });

      MenuButton btnBot = new MenuButton("Bot game");
      btnBot.setOnMouseClicked(event -> {
        /** enable bot playing */
        GameRoot.gameMode = "Auto";

        getChildren().add(menu2);
        TranslateTransition translateTransition =
            new TranslateTransition(Duration.seconds(TransTtDur), menu1);
        translateTransition.setToX(menu1.getTranslateX() - offset);
        TranslateTransition translateTransition1 =
            new TranslateTransition(Duration.seconds(TransTt1Dur), menu2);
        translateTransition1.setToX(menu1.getTranslateX());

        translateTransition.play();
        translateTransition1.play();

        translateTransition.setOnFinished(evt -> {
          getChildren().remove(menu1);
        });
      });

      MenuButton btnPlayer = new MenuButton("Player game");
      btnPlayer.setOnMouseClicked(event -> {
        GameRoot.gameMode = "Normal";

        getChildren().add(menu2);
        TranslateTransition translateTransition =
            new TranslateTransition(Duration.seconds(TransTtDur), menu1);
        translateTransition.setToX(menu1.getTranslateX() - offset);
        TranslateTransition translateTransition1 =
            new TranslateTransition(Duration.seconds(TransTt1Dur), menu2);
        translateTransition1.setToX(menu1.getTranslateX());

        translateTransition.play();
        translateTransition1.play();

        translateTransition.setOnFinished(evt -> {
          getChildren().remove(menu1);
        });
      });

      MenuButton btnHorror = new MenuButton("Horror");
      btnHorror.setOnMouseClicked(event -> {
        try {
          /** stop playing and change scene */
          GameRoot.difficulty = "Horror";
          menuMp.stop();
          gameWindow.show(theStage);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      MenuButton btnNightmare = new MenuButton("Nightmare");
      btnNightmare.setOnMouseClicked(event -> {
        try {
          GameRoot.difficulty = "Nightmare";
          /** stop playing and change scene */
          menuMp.stop();
          gameWindow.show(theStage);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      /** back to change bot/player playing */
      MenuButton btnBack1 = new MenuButton("Back");
      btnBack1.setOnMouseClicked(event -> {
        getChildren().add(menu1);

        TranslateTransition translateTransition =
            new TranslateTransition(Duration.seconds(TransTtDur), menu2);
        translateTransition.setToX(menu2.getTranslateX() + offset);

        TranslateTransition translateTransition1 =
            new TranslateTransition(Duration.seconds(TransTt1Dur), menu1);
        translateTransition1.setToX(menu2.getTranslateX());

        translateTransition.play();
        translateTransition1.play();

        translateTransition.setOnFinished(evt -> {
          getChildren().remove(menu2);
        });
      });

      MenuButton btnExit = new MenuButton("Exit");
      btnExit.setOnMouseClicked(event -> {
        System.exit(0);
      });

      menu0.getChildren().addAll(btnNewGame, btnRePlay, btnJSort, btnSSort, btnStats, btnReadNotation, btnExit);
      getChildren().add(menu0);

      menu1.getChildren().addAll(btnBot, btnPlayer, btnBack);
      menu2.getChildren().addAll(btnHorror, btnNightmare, btnBack1);
    }
  }

  /**
   * Creates a single menu button
   * 
   * @author badabum007
   */
  private static class MenuButton extends StackPane {
    /** button label */
    private Text text;

    /**
     * Creates a single button and adds button effects
     * 
     * @throws FileNotFoundException
     * @param name - button name
     */
    public MenuButton(String name) throws FileNotFoundException {
      /** setting font */
      text = new Text(name);
      text.getFont();
      /** load custom font */
      Font font;
      font = Font.loadFont(new FileInputStream(new File("res/fonts/Kankin.otf")), 20);
      text.setFont(font);
      text.setFill(Color.WHITE);

      /** adding rectangle over button */
      int rectWidth = 250, rectHeight = 30;
      Rectangle bg = new Rectangle(rectWidth, rectHeight);
      double opacityLevel = 0.5, blurLevel = 3.5;
      bg.setOpacity(opacityLevel);
      bg.setFill(Color.BLACK);
      bg.setEffect(new GaussianBlur(blurLevel));

      /** setting effects */
      setAlignment(Pos.CENTER_LEFT);
      getChildren().addAll(bg, text);

      /** rectangle shuffling on mouse entering/exiting */
      setOnMouseEntered(event -> {
        int translateButtonForwardX = 10;
        bg.setTranslateX(translateButtonForwardX);
        text.setTranslateX(translateButtonForwardX);
        bg.setFill(Color.WHITE);
        text.setFill(Color.BLACK);
      });

      setOnMouseExited(event -> {
        int translateButtonBackwardX = 0;
        bg.setTranslateX(translateButtonBackwardX);
        text.setTranslateX(translateButtonBackwardX);
        bg.setFill(Color.BLACK);
        text.setFill(Color.WHITE);
      });

      /** glow effect on mouse press */
      int dropShadowRange = 50;
      DropShadow drop = new DropShadow(dropShadowRange, Color.WHITE);
      drop.setInput(new Glow());
      setOnMousePressed(event -> setEffect(drop));
      setOnMouseReleased(event -> setEffect(null));
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
