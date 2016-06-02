package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo._
import scala.meta._

case object MutableCollectionImport extends Inspection {

  private object MutableExtractor {
    def unapply(mtree: Tree): Option[Problem] = {
      mtree match {
        case q"import ..$importersnel" => {
          if (importersnel.exists(_.toString.contains("mutable")))
            Some(Problem(mtree, "Mutable collection import", "", "", Seq.empty))
          else None
        }
        case _ => None
      }
    }
  }

  def inspect(mtree: scala.meta.Tree): Seq[Problem] = mtree.collect {
    case MutableExtractor(problem) => problem
  }
}
