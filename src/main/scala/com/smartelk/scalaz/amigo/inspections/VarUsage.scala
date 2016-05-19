package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{Problem, Inspection}
import scala.meta._

//todo: there are cases when 'var' usage is OK, e.g. inside Actor. Check it when have scalameta's semantic analysis at hand
case object VarUsage extends Inspection {
  def inspect(mtree: scala.meta.Tree): Seq[Problem] = mtree.collect {
    case t@ q"..$mods var ..$patsnel: $tpeopt = $expropt" => {
      Problem(t, "'var' usage", "Using 'var' definition", "Prefer 'val' to 'var'", Seq("http://stackoverflow.com/questions/1791408/what-is-the-difference-between-a-var-and-val-definition-in-scala"))
    }
    case t@ q"..$mods var ..$patsnel: $tpeopt" => {
      Problem(t, "'var' usage", "Using 'var' declaration", "Prefer 'val' to 'var'", Seq("http://stackoverflow.com/questions/1791408/what-is-the-difference-between-a-var-and-val-definition-in-scala"))
    }
  }
}
