package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{Problem, Inspection}
import scala.meta._

case object NestedCopyUsage extends Inspection {
  private val startDepthFrom = 0
  private val depth = 1

  def inspect(mtree: scala.meta.Tree): Seq[Problem] = mtree.collect {
    case NestedCopyProblemExtractor(problem)  => problem
  }

  private object NestedCopyProblemExtractor {
    def unapply(mtree: Tree): Option[Problem] = {
      findProblem(mtree, startDepthFrom)
    }
  }

  private def findProblem(mtree: Tree, level: Int): Option[Problem] = {
    mtree match {
      case Term.Apply(applyTerm1, applyArgs1) => {
        applyTerm1 match {
          case Term.Select(_, selectName1) if selectName1.toString == "copy" => {
            applyArgs1.map(arg => {
              arg match {
                case Term.Arg.Named(_, namedArg1) => {
                  (namedArg1, level) match {
                    case (Term.Apply(applyTerm2, _), `depth`) => {
                      applyTerm2 match {
                        case Term.Select(_, selectName2) if selectName2.toString == "copy" => {
                          Some(Problem(mtree,
                            "'Nested copy' usage",
                            "Updating child data structure via multiple nested 'copy' calls",
                            "Consider using Scalaz's 'Lens'. See: http://eed3si9n.com/learning-scalaz/Lens.html"
                            ))
                        }
                        case _ => None
                      }
                    }
                    case (newLevel@Term.Apply(_, _), level) if level < depth => findProblem(newLevel, level + 1)
                    case _ => None
                  }
                }
                case _ => None
              }
            }).headOption.flatten
          }
          case _ => None
        }
      }
      case _ => None
    }
  }
}
