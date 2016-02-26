package com.smartelk.scalaz.amigo

import com.smartelk.scalaz.amigo.inspections.ScalaOptionUse
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.{Phase, Global}

class AmigoPlugin(val global: Global) extends Plugin {
  override val name: String = "scalaz-amigo"
  override val description: String = "Compiler plugin for Scalaz style code inspections"
  object configuration extends Configuration { val g = global }

  val component = new PostTyperComponent(global, configuration.postTyperInspections)
  override val components: List[PluginComponent] = List(component)
}

class PostTyperComponent(val global: Global, val inspections: Seq[Inspection]) extends PluginComponent {
  require(inspections.forall(_.phase == InspectionPhase.PostTyperInspection), "Only inspections to be run after 'typer' phase are supported here")
  override val phaseName: String = "scalaz-amigo-after-typer"
  override val runsAfter: List[String] = List("typer")
  override val runsBefore = List[String]("patmat")

  override def newPhase(prev: Phase): StdPhase   = new StdPhase(prev) {
    override def apply(unit: global.CompilationUnit): Unit = {
      inspections.map(ins => ins.traverse(unit.body.asInstanceOf[ins.Tree]))
    }
  }
}

trait Configuration {
  val g: Global

  val inspections = Seq(
    new ScalaOptionUse(g)
  )

  val postTyperInspections = inspections.filter(_.phase == InspectionPhase.PostTyperInspection)
}

