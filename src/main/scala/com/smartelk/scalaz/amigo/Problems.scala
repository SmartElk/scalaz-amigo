package com.smartelk.scalaz.amigo

sealed trait Problem
sealed trait Message {
  val problem: Problem
}

case class Warning(problem: Problem, description: String, advice: String) extends Message

object Problems {
  case object ScalaSomeUsage extends Problem
  case object ScalaNoneUsage extends Problem
}
