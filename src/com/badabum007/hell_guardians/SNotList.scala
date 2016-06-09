package com.badabum007.hell_guardians

import scala.collection.JavaConversions
import scala.collection.mutable.Set
import scala.collection.mutable.Seq
import scala.collection.mutable.MutableList

class SNotList {
  val tempList: MutableList[Int] = MutableList()

  def addEl(element: Int) {
    tempList.+=(element)
  }

  def ret() :java.util.List[Int] = JavaConversions.mutableSeqAsJavaList(tempList)

}