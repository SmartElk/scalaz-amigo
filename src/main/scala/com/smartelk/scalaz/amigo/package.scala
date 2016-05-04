package com.smartelk.scalaz

import scala.reflect.internal.util.{RangePosition, SourceFile}

package object amigo {

  abstract class Inspection {
    def apply(mtree: scala.meta.Tree): Seq[Problem]
  }

  case class Problem(mtree: scala.meta.Tree, problem: String, description: String, advice: String, example: String)

  case class ProblemWithContext(problem: Problem, source: SourceFile) {
    val position = {
      val mPos = problem.mtree.position
      new RangePosition(source, mPos.start.offset, mPos.point.offset, mPos.end.offset)
    }
  }
}
