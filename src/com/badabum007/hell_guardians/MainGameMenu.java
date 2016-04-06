package com.badabum007.hell_guardians;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class MainGameMenu extends Application {

  //define the stage as public for communication between interface classes
  public Stage theStage = new Stage();

  //create object for our buttons
  private GameMenu gameMenu;

  private Pane root = new Pane();
  private GameWindow gameWindow = new GameWindow();

  //music player object
  private MediaPlayer menuMp;

  @Override
  public void start(Stage primaryStage) throws Exception {

    theStage = primaryStage;

    //menu content adding

    //adding music
    Media media= new Media(new File("res/music/Gonzalo_Varela_-_06_-_Abandoned_Souls.mp3").toURI().toString());
    menuMp = new MediaPlayer(media);
    //autostart when program is launched
    menuMp.setAutoPlay(true);
    //infinite song playing
    menuMp.setCycleCount(MediaPlayer.INDEFINITE);
    menuMp.play();
    MediaView mediaView = new MediaView(menuMp);

    int height = 600, width = 800;
    //window size
    root.setPrefSize(width, height);

    //setting background
    InputStream is = Files.newInputStream(Paths.get("res/images/main_menu.png"));
    Image img = new Image(is);
    ImageView imgView = new ImageView(img);
    imgView.setFitWidth(width);
    imgView.setFitHeight(height);

    //disable window resize
    theStage.setMinWidth(width);        
    theStage.setMinHeight(height);
    theStage.setMaxWidth(width);        
    theStage.setMaxHeight(height);

    //initializing our menu
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

  //menu class
  private class GameMenu extends Parent {
    public GameMenu() throws FileNotFoundException {
      int distBetweenButtons = 10;
      VBox menu0 = new VBox(distBetweenButtons);

      //setting menu position
      int menuTransX = 550, menuTransY = 450;
      menu0.setTranslateX(menuTransX);
      menu0.setTranslateY(menuTransY);

      MenuButton btnNewGame = new MenuButton("New game");
      btnNewGame.setOnMouseClicked(event -> {
        try {
          //stop playing and change scene
          menuMp.stop();
          gameWindow.show(theStage);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      MenuButton btnExit = new MenuButton("Exit");
      btnExit.setOnMouseClicked(event -> {
        System.exit(0);
      });

      menu0.getChildren().addAll( btnNewGame, btnExit);
      getChildren().add(menu0);
    }
  }

  //button class
  private static class MenuButton extends StackPane {
    private Text text;

    public MenuButton(String name) throws FileNotFoundException {
      //setting font
      text = new Text(name);
      text.getFont();
      Font font;
      font = Font.loadFont(new FileInputStream(new File("res/fonts/Kankin.otf")), 20);
      text.setFont(font); 
      text.setFill(Color.WHITE);

      //adding rectangle over button
      int rectWidth = 250, rectHeight = 30;
      Rectangle bg = new Rectangle(rectWidth, rectHeight);
      double opacityLevel = 0.5, blurLevel = 3.5;
      bg.setOpacity(opacityLevel);
      bg.setFill(Color.BLACK);
      bg.setEffect(new GaussianBlur(blurLevel));

      //setting effects
      setAlignment(Pos.CENTER_LEFT);
      getChildren().addAll(bg, text);

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