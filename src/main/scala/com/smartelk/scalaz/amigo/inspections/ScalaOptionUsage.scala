package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.Problems._
import com.smartelk.scalaz.amigo.{InspectionContext, Inspection}

class ScalaOptionUsage(c: InspectionContext) extends Inspection(c) {
  import context.global._

  override val inspect: Inspect =  {
    case Select(left, TermName("apply")) if (left.tpe.typeSymbol.fullName == "scala.Some") => {
      warning(left, ScalaOptionUsage("Some"), """Using Scala's 'Some' directly""", """Use Scalaz's 'some'""")
    }
    case Select(left, TermName("None")) if (left.tpe.typeSymbol.fullName == "scala")  => {
      warning(left, ScalaOptionUsage("None"), """Using Scala's 'None' directly""", """Use Scalaz's 'none'""")
    }
  }
}
