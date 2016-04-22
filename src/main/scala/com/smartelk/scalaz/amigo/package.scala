package com.smartelk.scalaz

import scala.reflect.internal.util.{Position, NoPosition}
import scala.util.Try

package object amigo {
  abstract class Inspection {
    def apply(mtree: scala.meta.Tree): Seq[Warning]
  }
  case class Warning(mtree: scala.meta.Tree, problem: String, description: String, advice: String, example: String)

  implicit class MtreeWrapper(t: scala.meta.Tree){
    def position: Position = {
      Try {
        import scala.language.reflectiveCalls
        val scratchpad = t.asInstanceOf[{ def internalScratchpad: Seq[Any] }].internalScratchpad
        val associatedGtree = scratchpad.collect { case gtree: scala.reflect.internal.SymbolTable#Tree => gtree }.head
        associatedGtree.pos
      }.getOrElse(NoPosition)
    }
  }
}
