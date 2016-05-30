package com.badabum007.hell_guardians
import java.io.File;

class QSortScala {
  def sort(currentArray: Array[Int], helpArray: Array[File]) {
    def swap(i: Int, j: Int) {
      val temp = currentArray(i)
      currentArray(i) = currentArray(j)
      currentArray(j) = temp
      val temp1 = helpArray(i)
      helpArray(i) = helpArray(j)
      helpArray(j) = temp1
    }

    def quickSort(low: Int, high: Int) {
      val pivot = currentArray((low + high) / 2)
      var i = low
      var j = high
      while (i <= j) {
        while (currentArray(i) > pivot) i += 1
        while (currentArray(j) < pivot) j -= 1
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (low < j) quickSort(low, j)
      if (j < high) quickSort(i, high)
    }
    quickSort(0, currentArray.length - 1)
  }
}