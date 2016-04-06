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

    //window size
    root.setPrefSize(800, 600);

    //setting background
    InputStream is = Files.newInputStream(Paths.get("res/images/main_menu.png"));
    Image img = new Image(is);
    ImageView imgView = new ImageView(img);
    imgView.setFitWidth(800);
    imgView.setFitHeight(600);

    //disable window resize
    theStage.setMinWidth(800);        
    theStage.setMinHeight(600);
    theStage.setMaxWidth(800);        
    theStage.setMaxHeight(600);

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
      VBox menu0 = new VBox(10);

      //setting menu position
      menu0.setTranslateX(550);
      menu0.setTranslateY(450);

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
      Rectangle bg = new Rectangle(250, 30);
      bg.setOpacity(0.5);
      bg.setFill(Color.BLACK);
      bg.setEffect(new GaussianBlur(3.5));

      //setting effects
      setAlignment(Pos.CENTER_LEFT);
      getChildren().addAll(bg, text);

      setOnMouseEntered(event -> {
        bg.setTranslateX(10);
        text.setTranslateX(10);
        bg.setFill(Color.WHITE);
        text.setFill(Color.BLACK);
      });

      setOnMouseExited(event -> {
        bg.setTranslateX(0);
        text.setTranslateX(0);
        bg.setFill(Color.BLACK);
        text.setFill(Color.WHITE);
      });

      DropShadow drop = new DropShadow(50, Color.WHITE);
      drop.setInput(new Glow());

      setOnMousePressed(event -> setEffect(drop));
      setOnMouseReleased(event -> setEffect(null));
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}