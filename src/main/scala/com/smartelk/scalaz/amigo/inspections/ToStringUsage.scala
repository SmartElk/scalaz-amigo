package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{Warning, Inspection}
import scala.meta._

class ToStringUsage extends Inspection {
  def apply(mtree: scala.meta.Tree): Seq[Warning] = mtree.topDown.collect {
    case q"$expr.${name: Term.Name}" if name.toString.trim == "toString" => {
      Warning(
        "'toString' usage",
        """Using 'toString' for conversion to textual representation""",
        """Use Scalaz's 'Show' typeclass. See: 'http://eed3si9n.com/learning-scalaz/Show.html', 'https://earldouglas.com/notes/learning-scalaz.html'""",
        """
          |import scalaz._
          |import Scalaz._
          |val a: String = 1.shows
        """.stripMargin)
    }
  }
}