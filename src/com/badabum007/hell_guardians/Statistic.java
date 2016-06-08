package com.badabum007.hell_guardians;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Statistic creation and screen output
 * 
 * @author pixxx
 *
 */
public class Statistic {
  /** Статистика по каждой клетке */
  int[][] statisticNumbers;

  /** Towers count */
  int sum;

  /** Для округления */
  final static int ROUND = 100;

  int averageTime;

  /** Scala Class содержащий статистику */
  SList listScala;

  int max;
  int offsX = 450;
  int offsY = 20;

  /**
   * Инициализация данных
   */
  public Statistic() {
    averageTime = 0;
    listScala = new SList();
    sum = 0;
    max = 0;
    statisticNumbers = new int[GameRoot.rows][GameRoot.columns];
    for (int i = 0; i < GameRoot.rows; i++) {
      for (int j = 0; j < GameRoot.columns; j++) {
        statisticNumbers[i][j] = 0;
      }
    }

    getStatisticNumbers();
  }

  /**
   * Метод считывает статистические данные о клетках и записывает их в массив
   */
  public void getStatisticNumbers() {
    int coordX, coordY;
    String line;
    File[] saveFiles = SaveManager.getSaveList();
    int tempTime;
    for (int i = 0; i < saveFiles.length; i++) {
      try {
        String[] args = new String[3];
        BufferedReader reader = new BufferedReader(new FileReader(saveFiles[i]));
        reader.readLine();
        while ((line = reader.readLine()) != null) {
          args = line.split(" ");
          coordX = (Integer.parseInt(args[0]) - GameWindow.offsetXY) / GameWindow.blockSize;
          coordY = (Integer.parseInt(args[1]) - GameWindow.offsetXY) / GameWindow.blockSize;
          tempTime = Integer.parseInt(args[2]);
          if (averageTime == 0) {
            averageTime = tempTime;
          } else {
            averageTime = (averageTime + tempTime) / 2;
          }
          statisticNumbers[coordY][coordX]++;
          if (statisticNumbers[coordY][coordX] > max) {
            max = statisticNumbers[coordY][coordX];
          }
          sum++;
        }
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    for (int i = 0; i < GameRoot.rows; i++) {
      for (int j = 0; j < GameRoot.columns; j++) {
        listScala.addEl((double)statisticNumbers[i][j]);
      }
    }

  }

  /**
   * Метод реализует вывод статистики на экран
   */
  public void showStatistic() {
    VBox vbox = new VBox();
    vbox.setTranslateX(offsX);
    vbox.setTranslateY(offsY);
    double color;
    double temp;
    Object obj;
    
    List<Object> arrlist = new ArrayList<Object>();
    arrlist = listScala.ret();
    ListIterator<Object> lIter =  arrlist.listIterator();

    for (int i = 0; i < GameRoot.rows; i++) {
      HBox hbox = new HBox();
      for (int j = 0; j < GameRoot.columns; j++) {
        
        obj = lIter.next();
        color = (double) obj / max;
        temp = (double) obj / sum * ROUND;
        int tempValue = (int) temp;
        
        temp = (double) tempValue / ROUND;
        Text boxText = new Text(Double.toString(temp * ROUND) + "%");
        boxText.setFill(Color.WHITE);
        Rectangle bg = new Rectangle(GameWindow.blockSize / 2, GameWindow.blockSize / 2);
        bg.setFill(Color.color(0.0, color, 0.0));
        bg.setOpacity(1);
        StackPane sPane = new StackPane();
        sPane.getChildren().add(bg);
        sPane.getChildren().add(boxText);
        hbox.getChildren().add(sPane);
      }
      vbox.getChildren().add(hbox);
    }
    HBox hbox = new HBox();
    Rectangle bg = new Rectangle(GameWindow.blockSize / 2 * GameRoot.columns, GameWindow.blockSize / 2);
    bg.setFill(Color.color(0.0, 0.0, 0.0));
    bg.setOpacity(1);
    StackPane sPane = new StackPane();
    Text boxText = new Text("Average time: " + Integer.toString(averageTime));
    boxText.setFill(Color.WHITE);
    sPane.getChildren().add(bg);
    sPane.getChildren().add(boxText);
    hbox.getChildren().add(sPane);
    vbox.getChildren().add(hbox);
    
    MainGameMenu.root.getChildren().add(vbox);
  }

}

