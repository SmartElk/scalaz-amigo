package com.smartelk.scalaz.amigo.inspections

//import com.smartelk.scalaz.amigo.{Inspection, InspectionContext}

/*
class ToStringUsage(c: InspectionContext) extends Inspection(c) {
  import context.global._

  override val inspect: Inspect =  {
    case Select(left, TermName("toString")) if (left.tpe.typeSymbol.fullName != "scalaz.Cord") => {
      warning(left,
        "'toString' usage",
        """Using 'toString' for conversion to textual representation""",
        """Use Scalaz's 'Show' typeclass. See: 'http://eed3si9n.com/learning-scalaz/Show.html', 'https://earldouglas.com/notes/learning-scalaz.html'""",
        """
          |import scalaz._
          |import Scalaz._
          |val a: String = 1.shows
        """.stripMargin)
    }
  }
}*/
