package com.smartelk.scalaz.amigo

sealed trait Problem

object Problems {
  case object ScalaSomeUsage extends Problem
  case object ScalaNoneUsage extends Problem
  case object DualEqualityUsage extends Problem
}