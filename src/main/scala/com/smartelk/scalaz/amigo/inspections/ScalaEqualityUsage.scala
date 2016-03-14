package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.Problems._
import com.smartelk.scalaz.amigo.{Inspection, InspectionContext}

class ScalaEqualityUsage(c: InspectionContext) extends Inspection(c) {
  import context.global._

  override val inspect: Inspect =  {
    case Select(left, TermName("$eq$eq")) => {
      warning(left, ScalaEqualityUsage("=="), """Using '==' is type unsafe""", """Use Scalaz's 'Equal' typeclass and '===' operator""")
    }
    case Select(left, TermName("$bang$eq")) => {
      warning(left, ScalaEqualityUsage("!="), """Using '!=' is type unsafe""", """Use Scalaz's 'Equal' typeclass and '=/=' operator""")
    }
  }
}
