package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo._

class EitherUsage(c: InspectionContext) extends Inspection(c) {
  import context.global._

  override val inspect: Inspect =  {
    case Select(left, TermName("apply")) if (left.tpe.typeSymbol.fullName == "scala.util.Right") => {
      warning(left,
        "'Right' usage",
        """Using Scala's standard 'Either' type ('Right' in this case)""",
        """Use Scalaz's '\/' type. See: 'http://eed3si9n.com/learning-scalaz-day7'""",
        """
          |import scalaz._
          |import Scalaz._
          |val a: \/[String,Int] = 123.right[String]
        """.stripMargin)
    }
    case Select(left, TermName("apply")) if (left.tpe.typeSymbol.fullName == "scala.util.Left") => {
      warning(left,
        "'Left' usage",
        """Using Scala's standard 'Either' type ('Left' in this case)""",
        """Use Scalaz's '\/' type. See: 'http://eed3si9n.com/learning-scalaz-day7'""",
        """
          |import scalaz._
          |import Scalaz._
          |val a: \/[String,Int] = "bla".left[Int]
        """.stripMargin)
    }
  }
}
