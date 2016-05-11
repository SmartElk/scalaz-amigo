package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo._
import scala.meta._

case object EitherUsage extends Inspection {
  def inspect(mtree: scala.meta.Tree): Seq[Problem] = mtree.collect {
    case t@ q"${name: Term.Name}" if name.toString == "Right" => {
      Problem(t,
        "'Right' usage",
        """Using Scala's standard 'Either' type ('Right' in this case)""",
        """Use Scalaz's '\/' type. See: 'http://eed3si9n.com/learning-scalaz-day7'""",
        """
            |import scalaz._
            |import Scalaz._
            |val a: \/[String,Int] = 123.right[String]
          """.stripMargin)
      }
    case t@ q"${name: Term.Name}" if name.toString == "Left" => {
        Problem(t,
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

