package com.badabum007.hell_guardians

import scala.collection.JavaConversions
import scala.collection.mutable.Set
import scala.collection.mutable.Seq
import scala.collection.mutable.MutableList

class SList {
  val tempList: MutableList[Double] = MutableList()

  def addEl(element: Double) {
    tempList.+=(element)
  }

  def ret(): java.util.List[Double] = {
    JavaConversions.mutableSeqAsJavaList(tempList)
  }

/*  def getElem(indexX: Int, indexY: Int, size: Int){
    val temp: Int = indexX * size + indexY
    val k: Option[Int] = tempList.get(temp)
  }*/
}