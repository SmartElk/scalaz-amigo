package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo._

class OptionUsage(c: InspectionContext) extends Inspection(c) {
  import context.global._

  override val inspect: Inspect =  {
    case Select(left, TermName("apply")) if (left.tpe.typeSymbol.fullName == "scala.Some") => {
      warning(left,
        "'Some' usage",
        """Using Scala's standard 'Some'""",
        """Use Scalaz's 'some'. See: 'http://eed3si9n.com/scalaz-cheat-sheet'""",
        """
          |import scalaz._
          |import Scalaz._
          |val a: Option[Int] = 1.some
        """.stripMargin)
    }

    case Select(left, TermName("None")) if (left.tpe.typeSymbol.fullName == "scala")  => {
      warning(left,
        "'None' usage",
        """Using Scala's standard 'None'""",
        """Use Scalaz's 'none'. See: 'http://eed3si9n.com/scalaz-cheat-sheet'""",
        """
          |import scalaz._
          |import Scalaz._
          |val a: Option[Int] = none[Int]
        """.stripMargin)
    }
  }
}
