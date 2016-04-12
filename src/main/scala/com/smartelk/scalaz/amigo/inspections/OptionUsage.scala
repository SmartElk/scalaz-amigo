package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{Inspection, Warning}
import scala.meta._

class OptionUsage extends Inspection {
  def apply(mtree: scala.meta.Tree): Seq[Warning] = mtree.topDown.collect {
    case q"${name: Term.Name}" if name.toString == "Some" => {
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
    case q"${name: Term.Name}" if name.toString == "None" => {
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
}
