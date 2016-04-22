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
    override val runsAfter: List[String] = List("typer")

    override def newPhase(prev: Phase): StdPhase = new StdPhase(prev) {
      override def apply(unit: global.CompilationUnit): Unit = {
        val mtree = unit.source.content.parse[Source].get
        val result = inspections.flatMap(_(mtree))
        applyOnInspectionResult(result)
      }
    }
  }

  def applyOnInspectionResult(result: Seq[Warning]) = {
    result.foreach {r =>
      global.reporter.warning(r.mtree.position, r.description)
    }
  }

  val inspections = Seq(
    new OptionUsage,
    new EitherUsage,
    new EqualityUsage,
    new ToStringUsage,
    new VarUsage
  )
}



