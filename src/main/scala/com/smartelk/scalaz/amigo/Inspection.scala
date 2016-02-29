package com.smartelk.scalaz.amigo

import scala.tools.nsc.Global

abstract class Inspection(val global: Global) {
  type InspectionPhase = InspectionPhase.Value
  type Tree = global.Tree

  val phase: InspectionPhase = InspectionPhase.PostTyperInspection
  def check(tree: Tree): Seq[FoundProblem]

  def traverse(tree: Tree): Seq[FoundProblem] = {
    import global._
    tree match {
      case DefDef(mods, _, _, _, _, _) if tree.symbol.isSynthetic => Seq()
      case _ => check(tree)
    }
  }
}

object InspectionPhase extends Enumeration {
  type InspectionPhase = Value
  val PostTyperInspection = Value
}

sealed trait FoundProblem
case class Warning(message: String) extends FoundProblem