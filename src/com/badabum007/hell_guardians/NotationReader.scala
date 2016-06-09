package com.badabum007.hell_guardians

import scala.collection.JavaConverters._

class NotationReader {
  def read(gameInfo: java.util.List[Int]): String = {
    
    val gameInfoL = gameInfo.asScala.toList
    var result: String = " Difficulty: "
    var i = 0
    
    gameInfoL(i) match{
      case 25 =>
        result += "Horror\n"
      case _  => 
        result +="Nightmare\n"
    }
    
    for (i <- 1 to gameInfo.size - 1 by 3) {
        result+=("Tower built at: [" + (gameInfoL(i + 1)-GameWindow.offsetXY)/GameWindow.blockSize +
            "][" + (gameInfoL(i)-GameWindow.offsetXY)/GameWindow.blockSize + "] cell.   At " + gameInfoL(i + 2)  + " second.\n")
    }
    
    println(result)
    result
  }
    
}