package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{Problem, Inspection}
import scala.meta._

//todo: examine imports to check for Ex: 'collection.mutable.ArrayBuffer', 'mutable.ArrayBuffer', 'ArrayBuffer'
case object MutableCollectionUsage extends Inspection {
  def inspect(mtree: scala.meta.Tree): Seq[Problem] = mtree.collect {
    case q"$expr.$name" if expr.toString.contains("scala.collection.mutable") => {
      Problem(mtree, "Mutable collection usage", "", "", Seq.empty)
    }
  }.distinct
}
