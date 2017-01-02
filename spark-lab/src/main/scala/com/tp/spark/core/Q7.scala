package com.tp.spark.core

object Q7 {

  def decode(l : List[(Int, Char)]) = {
    l.flatMap{ case (n, c) => (1 to n).map{x => c}   }
  }

}
