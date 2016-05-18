package com.smartelk.scalaz

import scala.reflect.internal.util.{RangePosition, SourceFile}

package object amigo {

  abstract class Inspection extends Product {
    def inspect(mtree: scala.meta.Tree): Seq[Problem]
  }

  case class Problem(mtree: scala.meta.Tree, problem: String, description: String, advice: String)

  case class ProblemWithContext(problem: Problem, source: SourceFile) {
    val position = {
      val mPos = problem.mtree.position
      new RangePosition(source, mPos.start.offset, mPos.point.offset, mPos.end.offset)
    }
  }
}
