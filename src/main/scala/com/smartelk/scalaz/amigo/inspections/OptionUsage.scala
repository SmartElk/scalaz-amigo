package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{Inspection, Warning}
import scala.meta.tql._
import scala.meta.internal.ast._
import scala.language.reflectiveCalls

class OptionUsage extends Inspection {

  def apply(unit: scala.meta.Tree): Seq[Warning] = unit.topDown.collect {
   case Term.Apply(Term.Name("Some"), _) => {
      Warning(
        "'Some' usage",
        """Using Scala's standard 'Some'""",
        """Use Scalaz's 'some'. See: 'http://eed3si9n.com/scalaz-cheat-sheet'""",
        """
          |import scalaz._
          |import Scalaz._
          |val a: Option[Int] = 1.some
        """.stripMargin)
    }
    case Term.Name("None") => {
      Warning(
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

 /* def apply = collect {
    case t @ Term.Select(Term.Apply(Term.Name("List"), l), Term.Name("toSet")) => "Message!"
  }.topDown*/
}


/*class OptionUsage(c: InspectionContext) extends Inspection(c) {
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
}*/
