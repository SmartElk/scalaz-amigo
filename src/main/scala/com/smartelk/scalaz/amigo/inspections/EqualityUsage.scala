package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo._

class EqualityUsage(c: InspectionContext) extends Inspection(c) {
  import context.global._

  override val inspect: Inspect =  {
    case Select(left, TermName("$eq$eq")) => {
      warning(left,
        "'==' usage",
        """Using '==' is not type safe""",
        """Use Scalaz's 'Equal' typeclass and '===' operator. See: 'http://eed3si9n.com/learning-scalaz/Equal.html', 'https://earldouglas.com/notes/learning-scalaz.html'""",
        """
          |import scalaz._
          |import Scalaz._
          |1 === 1
        """.stripMargin
        )
    }

    case Select(left, TermName("$bang$eq")) => {
      warning(left,
        "'!=' usage",
        """Using '!=' is not type safe""",
        """Use Scalaz's 'Equal' typeclass and '=/=' operator. See: 'http://eed3si9n.com/learning-scalaz/Equal.html', 'https://earldouglas.com/notes/learning-scalaz.html'""",
        """
          |import scalaz._
          |import Scalaz._
          |1 =/= 2
        """.stripMargin
        )
    }
  }
}
