package com.badabum007.hell_guardians;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

  SaveManager(String name) {
    tempFileName = name;
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
  public void createSaveFile() throws IOException {
    String fileName =
        saveDir + new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime()) + "_"
            + GameWindow.gameRoot.towers.size() + extension;
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
}
