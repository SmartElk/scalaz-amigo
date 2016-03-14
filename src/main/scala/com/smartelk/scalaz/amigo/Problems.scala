package com.smartelk.scalaz.amigo

sealed trait Problem {
  val internalDetails: String
}

object Problems {
  case class ScalaOptionUsage(internalDetails: String) extends Problem
  case class ScalaEqualityUsage(internalDetails: String) extends Problem
}