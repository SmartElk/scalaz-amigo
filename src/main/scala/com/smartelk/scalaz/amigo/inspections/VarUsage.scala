package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{Warning, Inspection}
import scala.meta._

//todo: there are cases when 'var' usage is OK, e.g. inside Actor. Check it when have scalameta's semantic analysis at hand
class VarUsage extends Inspection {
  def apply(mtree: scala.meta.Tree): Seq[Warning] = mtree.topDown.collect {
    case q"..$mods var ..$patsnel: $tpeopt = $expropt" => {
      Warning("'var' usage", "Using 'var' definition", "Prefer 'val' to 'var'", "")
    }
    case q"..$mods var ..$patsnel: $tpeopt" => {
      Warning("'var' usage", "Using 'var' declaration", "Prefer 'val' to 'var'", "")
    }
  }
}
