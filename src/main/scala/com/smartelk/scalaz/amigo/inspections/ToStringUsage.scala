package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{Problem, Inspection}
import scala.meta._

class ToStringUsage extends Inspection {
  def apply(mtree: scala.meta.Tree): Seq[Problem] = mtree.collect {
    case t@ q"$expr.${name: Term.Name}" if name.toString.trim == "toString" => {
      Problem(t,
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