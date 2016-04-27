package com.badabum007.hell_guardians

object TestingWsheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
    def fib1( n : Int) : Int = {
    if (n < 2){
      1
    }
    else {
      fib1(n - 1) + fib1(n - 2)
    }
  }                                               //> fib1: (n: Int)Int
  fib1(5)                                         //> res0: Int = 8
    def fib2( n : Int ) : Int = {
      var a = 0
          var b = 1
          var i = 0
          var c = 0

          while( i < n ) {
             c = a + b
                a = b
                b = c
                i = i + 1
          }
      return c
  }                                               //> fib2: (n: Int)Int
  fib2(5);                                        //> res1: Int = 8
}