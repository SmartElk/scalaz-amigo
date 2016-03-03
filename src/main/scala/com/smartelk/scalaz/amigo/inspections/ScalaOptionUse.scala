package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.Problems._
import com.smartelk.scalaz.amigo.{InspectionContext, Inspection}

class ScalaOptionUse(c: InspectionContext) extends Inspection(c) {
  import context.global._

  override val inspect: Inspect = {
    case Select(left, TermName("apply")) if (left.tpe.typeSymbol.fullName == "scala.Some") => {
      c.warning(ScalaSomeUsage, """Using Scala's "Some" directly""", """Use Scalaz's "some"""")
    }
  }
}
