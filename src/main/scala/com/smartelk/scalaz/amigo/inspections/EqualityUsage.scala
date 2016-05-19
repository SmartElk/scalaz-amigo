package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo._
import scala.meta._

case object EqualityUsage extends Inspection {
  def inspect(mtree: scala.meta.Tree): Seq[Problem] = mtree.collect {
    case t@ q"${name: Term.Name}" if name.toString == "==" => {
      Problem(t,
        "'==' usage",
        "Using '==' is not type safe",
        "Use Scalaz's 'Equal' typeclass and '===' operator",
        Seq("http://eed3si9n.com/learning-scalaz/Equal.html", "https://earldouglas.com/notes/learning-scalaz.html")
      )
    }
    case t@ q"${name: Term.Name}" if name.toString == "!=" => {
      Problem(t,
        "'!=' usage",
        "Using '!=' is not type safe",
        "Use Scalaz's 'Equal' typeclass and '=/=' operator",
        Seq("http://eed3si9n.com/learning-scalaz/Equal.html", "https://earldouglas.com/notes/learning-scalaz.html")
      )
    }
  }
}
