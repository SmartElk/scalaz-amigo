package com.smartelk.scalaz.amigo

import com.smartelk.scalaz.amigo.inspections._
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc._
import scala.meta._

class AmigoPlugin(val global: Global) extends Plugin {
  val self = this

  override val components: List[PluginComponent] = List(AmigoComponent)
  val name = "scalaz-amigo"
  val description = "Compiler plugin for Scalaz code-style inspections"

  object AmigoComponent extends PluginComponent {
    override val global: self.global.type = self.global

    override val phaseName: String = "scalaz-amigo"
    override val runsAfter: List[String] = List("refchecks")

    override def newPhase(prev: Phase): StdPhase = new StdPhase(prev) {
      override def apply(unit: global.CompilationUnit): Unit = {
        val mtree = unit.source.content.parse[Source].get
        val problems = inspections.flatMap(_(mtree))
        val problemsWithContext = problems.map(ProblemWithContext(_, unit.source))
        applyOnInspectionResult(problemsWithContext)
      }
    }
  }

  def applyOnInspectionResult(problems: Seq[ProblemWithContext]) = {
    problems.foreach {p =>
      global.reporter.warning(p.position, p.problem.description)
    }
  }

  override def init(options: List[String], error: (String) => Unit): Boolean = {
    //todo
    true
  }

  override val optionsHelp: Option[String] = {
    //todo
    None
  }

  val inspections = Seq(
    new OptionUsage,
    new EitherUsage,
    new EqualityUsage,
    new ToStringUsage,
    new VarUsage
  )
}



