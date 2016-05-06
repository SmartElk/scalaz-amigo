package com.smartelk.scalaz.amigo

import com.smartelk.scalaz.amigo.inspections._
import scala.reflect.internal.util.NoPosition
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc._
import scala.meta._

class AmigoPlugin(val global: Global) extends Plugin {
  val self = this

  override val components: List[PluginComponent] = List(AmigoComponent)
  val name = "scalaz-amigo"
  val description = "Compiler plugin for Scalaz code-style inspections"
  var alertMode = AlertMode.Warn

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

  object AlertMode extends Enumeration {
    val Warn, Error = Value
  }

  def applyOnInspectionResult(problems: Seq[ProblemWithContext]) = {
    val alert = alertMode match {
      case AlertMode.Warn => global.reporter.warning _
      case AlertMode.Error => global.reporter.error _
    }
    problems.foreach(p => alert(p.position, p.problem.description))
  }

  override def init(options: List[String], error: (String) => Unit): Boolean = {
    options.foreach { opt =>
        opt.split(':').toList match {
          case "alertMode" :: value:: Nil => value match {
            case "error" => alertMode = AlertMode.Error
            case "warn" => alertMode = AlertMode.Warn
            case unknown => global.reporter.error(NoPosition, "Wrong scalaz-amigo plugin 'alertMode' option: " + unknown)
          }
          case _ =>
        }
      }
    true
  }

  override val optionsHelp: Option[String] = Some("""
      | -P:scalaz-amigo:
      |   alertMode:    warn/error
    """)

  val inspections = Seq(
    new OptionUsage,
    new EitherUsage,
    new EqualityUsage,
    new ToStringUsage,
    new VarUsage
  )
}
