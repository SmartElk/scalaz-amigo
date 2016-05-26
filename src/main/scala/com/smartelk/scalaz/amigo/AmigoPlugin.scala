package com.smartelk.scalaz.amigo

import com.smartelk.scalaz.amigo.inspections._
import scala.reflect.internal.util.NoPosition
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc._
import scala.meta._
import shapeless._
import syntax.std.traversable._

class AmigoPlugin(val global: Global) extends Plugin {
  val self = this

  override val components: List[PluginComponent] = List(AmigoComponent)
  val name = "scalaz-amigo"
  val description = "Compiler plugin for Scalaz code-style inspections"
  var alertMode = AlertMode.Warn
  var turnedOffInspections: List[Inspection] = Nil

  object AmigoComponent extends PluginComponent {
    override val global: self.global.type = self.global

    override val phaseName: String = "scalaz-amigo"
    override val runsAfter: List[String] = List("typer")

    override def newPhase(prev: Phase): StdPhase = new StdPhase(prev) {
      override def apply(unit: global.CompilationUnit): Unit = {
        val mtree = unit.source.content.parse[Source].get
        val problems = inspections.filter(!turnedOffInspections.contains(_)).flatMap(_.inspect(mtree))
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
    problems.foreach(p => alert(p.position, {
      s"###scalaz-amigo: ${p.problem.description}. Advice: ${p.problem.advice}" + {
          if (!p.problem.links.isEmpty) s"\nSee: ${p.problem.links.mkString(",")}"
          else ""
        }
    }))
  }

  override def init(options: List[String], error: (String) => Unit): Boolean = {
    options.foreach { opt =>
        opt.split(optionKeyValueSeparator).map(_.trim).toHList[String :: String :: HNil] match {
          case Some("alertMode" :: value:: HNil) => value match {
            case "error" => alertMode = AlertMode.Error
            case "warn" => alertMode = AlertMode.Warn
            case unknown => global.reporter.error(NoPosition, "Wrong scalaz-amigo plugin 'alertMode' option: " + unknown)
          }
          case Some("turnOff" :: value :: HNil) =>
            turnedOffInspections = value.split(optionMultipleValuesSeparator).filter(!_.isEmpty).map(t => inspections.find(_.productPrefix == t.trim)).flatten.toList
          case _ =>
        }
      }
    true
  }

  private val optionKeyValueSeparator = ':'
  private val optionMultipleValuesSeparator = '+'
  override val optionsHelp: Option[String] = Some(s"""
      | -P:scalaz-amigo:
      |   alertMode$optionKeyValueSeparator    {warn/error}
      |   turnOff$optionKeyValueSeparator      {list of inspection names separated by '$optionMultipleValuesSeparator'}
    """)

  val inspections = Seq(
    OptionUsage,
    EitherUsage,
    EqualityUsage,
    ToStringUsage,
    VarUsage,
    NestedCopyUsage,
    MutableCollectionUsage
  )
}
