package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{Warning, InspectionContext, Inspection}

class ScalaOptionUse(c: InspectionContext) extends Inspection(c) {
  import context.global._

  override val inspect: Inspect = {
    case Select(left, _) if (left.tpe.typeSymbol.fullName == "scala.Some") => {
      c.problem(Warning("""Using Scala's "Some" directly""", """Use Scalaz's "some""""))
    }
  }
}
