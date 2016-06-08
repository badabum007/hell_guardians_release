package com.badabum007.hell_guardians;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Class, that controls game saving operations
 * 
 * @author badabum007
 */
public class SaveManager {
  /** temp save file */
  String tempSave;

  String tempFileName;
  String dateFormat = "yyyyMMdd_HHmmss";
  String extension = ".sav";
  static String saveDir = "saves/";
  static String loadGameSave;
  int saveNameParts = 3;
  int dataArraySize = 2;
  int filesToGen = 10;
  String tempName = "logs";
  int rndValue = 10000;

  SaveManager(String name) {
    tempFileName = name;
  }

  SaveManager() {
    tempFileName = tempName;
  }

  /**
   * Creates a file with a specified name
   * 
   * @param fileName - name of file
   */
  public void createTempFile(String fileName) {
    File tempFile = new File(fileName);
    try {
      FileWriter out = new FileWriter(tempFile.getAbsoluteFile());
      out.write("");
      out.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    try {
      tempFile.createNewFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes string to a file
   * 
   * @param fileName - name of file
   * @param stringToAdd - string to add
   */
  public void addToFile(String fileName, String stringToAdd) {
    File file = new File(fileName);
    try {
      FileWriter out = new FileWriter(file.getAbsoluteFile(), true);
      out.write(stringToAdd);
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates a game save file
   * 
   * @throws IOException
   */
  public void createSaveFile(int size) throws IOException {
    String fileName =
        saveDir + new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime()) + "_"
            + size + "_" +((Integer)(new Random().nextInt(rndValue))) + extension;
    File file = new File(fileName);
    try {
      file.createNewFile();
      BufferedReader reader = new BufferedReader(new FileReader(tempFileName));
      FileWriter out = new FileWriter(new File(fileName).getAbsoluteFile(), true);
      String line;
      out.write(Shot.damage + "\n");
      while ((line = reader.readLine()) != null) {
        out.write(line + "\n");
      }
      out.close();
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * get array of save files
   * 
   * @return allFiles - save files array
   */
  public static File[] getSaveList() {
    File[] saveList;
    File filesPath = new java.io.File(new File(saveDir).getAbsolutePath());
    saveList = filesPath.listFiles();
    return saveList;
  }

  /**
   * save files list sorting - Java Quick Sort;
   */
  public File[] getSortedJavaList() {
    File[] saveFiles = getSaveList();
    int[] dataArrayOfFileList = new int[saveFiles.length];
    for (int i = 0; i < saveFiles.length; i++) {
      dataArrayOfFileList[i] = getResults(saveFiles[i].getName());
    }
    quickSort(dataArrayOfFileList, saveFiles, 0, dataArrayOfFileList.length - 1);
    return saveFiles;
  }

  /**
   * Java Qsort
   * 
   * @param arr - money array
   * @param files - filenames array
   */
  void quickSort(int arr[], File files[], int left, int right) {
    int index = partition(arr, files, left, right);
    if (left < index - 1) {
      quickSort(arr, files, left, index - 1);
    }
    if (index < right) {
      quickSort(arr, files, index, right);
    }
  }

  /**
   * get results array
   * 
   * @param nameOfFile - filename
   */
  private Integer getResults(String filename) {
    String[] array = filename.split("_");
    array[saveNameParts - 1] = array[saveNameParts - 1].replace(extension, "");
    return Integer.parseInt(array[saveNameParts - 1]);
  }

  /**
   * finding index
   * 
   * @param arr - money array
   * @param files - filenames array
   */
  int partition(int arr[], File files[], int left, int right) {
    int i = left, j = right;
    int tmp;
    File temp;
    int pivot = arr[(left + right) / 2];
    while (i <= j) {
      while (arr[i] < pivot) {
        i++;
      }
      while (arr[j] > pivot) {
        j--;
      }
      if (i <= j) {
        tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;

        temp = files[i];
        files[i] = files[j];
        files[j] = temp;

        i++;
        j--;
      }
    } ;
    return i;
  }

  /**
   * Scala Quick Sort
   * 
   */
  public File[] getSortedScalaList() {
    File[] saveFiles = getSaveList();
    int[] dataArrayOfFileList = new int[saveFiles.length];
    for (int i = 0; i < saveFiles.length; i++) {
      dataArrayOfFileList[i] = getResults(saveFiles[i].getName());
    }
    QSortScala qSortObject = new QSortScala();
    qSortObject.sort(dataArrayOfFileList, saveFiles);
    return saveFiles;
  }

  /**
   * generates save games
   * 
   * @param num - number of saves to generate
   * 
   */
  void generateSaves(int num) {
    Random rnd = new Random();
    long time;
    filesToGen = num;
    int currentCount;
    int coordX = 0;
    int coordY = 0;
    int posX;
    int posY;
    int randTime = 50;
    int dropNum = 2;
    int undropChance = 8;
    boolean[][] towerMap = new boolean[GameRoot.rows][GameRoot.columns];

    for (int saveCounter = 0; saveCounter < filesToGen; saveCounter++) {
      
      for (int i = 0; i < GameRoot.rows; i++) {
        for (int j = 0; j < GameRoot.columns; j++) {
          towerMap[i][j] = false;
        }
      }
      
      currentCount = 0;
      time = 0;
      Shot.damage = 25;
      createTempFile(tempFileName);

      while (true) {
        if (currentCount >= GameRoot.rows * GameRoot.columns) {
          break;
        }

        time += rnd.nextInt(randTime);

        /** check if the block is free */
        do {
          coordY = (int) (new Random().nextInt(GameRoot.rows));
          coordX = (int) (new Random().nextInt(GameRoot.columns));
        } while (towerMap[coordY][coordX] == true);
        for (int i = 0; i < coordX; i++) {
          if (towerMap[coordY][i] == false) {
            coordX = i;
            break;
          }
        }
        towerMap[coordY][coordX] = true;

        posX = coordX * GameWindow.blockSize + GameWindow.offsetXY;
        posY = coordY * GameWindow.blockSize + GameWindow.offsetXY;

        addToFile(tempFileName, posX + " " + posY + " " + time + "\n");
        currentCount++;

        if (rnd.nextInt(undropChance) == dropNum) {
          break;
        }
      }
      
      try {
        createSaveFile(currentCount);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }

}
