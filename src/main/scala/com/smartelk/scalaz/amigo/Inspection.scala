package com.smartelk.scalaz.amigo

import scala.tools.nsc.Global

abstract class Inspection(protected val context: InspectionContext) {
  type Tree = context.global.Tree
  type Inspect = PartialFunction[Tree, Unit]

  protected val inspect: Inspect

  val traversal = new context.global.Traverser {
    private def lookOver(tree: Tree) = {
      if (inspect.isDefinedAt(tree)) inspect(tree)
      else super.traverse(tree)
    }

    override def traverse(tree: Tree): Unit = {
      import context.global._
      tree match {
        case DefDef(mods, _, _, _, _, _) if tree.symbol.isSynthetic => Seq()
        case _ => lookOver(tree)
      }
    }
  }
}

case class InspectionContext(val global: Global) {
  val problems = scala.collection.mutable.Buffer[Problem]()
  def problem(p: Problem) = problems += p
}

trait Problem
case class Warning(cause: String, advice: String) extends Problem