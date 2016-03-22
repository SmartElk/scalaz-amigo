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
        case DefDef(mods, _, _, _, _, _) if tree.symbol.isSynthetic =>
        case _ => lookOver(tree)
      }
    }
  }

  def warning(tree: Tree, problem: String, description: String, advice: String, example: String = "") = context.messages += context.Warning(tree, problem, description, advice, example)
}

case class InspectionContext(val global: Global) {
  val messages = scala.collection.mutable.Buffer[Message]()
  case class Warning(tree: global.Tree, problem: String, description: String, advice: String, example: String) extends Message
}

trait Message {
  val problem: String
}