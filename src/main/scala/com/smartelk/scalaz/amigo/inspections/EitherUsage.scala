package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo._
import scala.meta._

class EitherUsage extends Inspection {
  def apply(mtree: scala.meta.Tree): Seq[Warning] = mtree.topDown.collect {
    case q"${name: Term.Name}" if name.toString == "Right" => {
      Warning(
        "'Right' usage",
        """Using Scala's standard 'Either' type ('Right' in this case)""",
        """Use Scalaz's '\/' type. See: 'http://eed3si9n.com/learning-scalaz-day7'""",
        """
            |import scalaz._
            |import Scalaz._
            |val a: \/[String,Int] = 123.right[String]
          """.stripMargin)
      }
      case q"${name: Term.Name}" if name.toString == "Left" => {
        Warning(
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

