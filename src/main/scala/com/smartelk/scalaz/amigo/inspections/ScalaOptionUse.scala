package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{FoundProblem, Inspection}
import scala.tools.nsc.Global

class ScalaOptionUse(global: Global) extends Inspection(global) {
  override def check(tree: Tree): Seq[FoundProblem] = {
    ???
  }
}
