package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo._
import scala.meta._

class EqualityUsage extends Inspection {
  def apply(mtree: scala.meta.Tree): Seq[Problem] = mtree.collect {
    case t@ q"${name: Term.Name}" if name.toString == "==" => {
      Problem(t,
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
    case t@ q"${name: Term.Name}" if name.toString == "!=" => {
      Problem(t,
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
