package com.smartelk.scalaz.amigo.inspections

import scala.meta.tql._
import scala.meta.internal.ast._
import scala.language.reflectiveCalls

class MetaInspection() {

  def apply = collect {
    case t @ Term.Select(Term.Apply(Term.Name("List"), l), Term.Name("toSet")) => "Message!"
  }.topDown
}
