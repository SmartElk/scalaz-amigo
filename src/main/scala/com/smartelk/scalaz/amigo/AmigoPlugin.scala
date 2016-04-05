package com.smartelk.scalaz.amigo

//import scala.meta.internal.hosts.scalac.contexts.Adapter

import scala.meta.Context
import scala.meta.artifacts.Artifact
import scala.meta.semantic.Context
import scala.tools.nsc.plugins.{PluginComponent}
import scala.meta.internal.hosts.scalac.{Plugin => ScalahostPlugin, Adapter}
import scala.tools.nsc._

class AmigoPlugin(g: Global) extends ScalahostPlugin(g) with AmigoPhase {
  override val components: List[PluginComponent] = List(ConvertComponent, AmigoComponent)
  var applyToInspectionContextAfterInspection: (Seq[Warning] => Unit) = (_ => ())
}

trait AmigoPhase {
  self: AmigoPlugin =>

  object AmigoComponent extends PluginComponent {
    val global: self.global.type = self.global
    import global._

    override val phaseName: String = "scalaz-amigo"
    override val runsAfter: List[String] = List("convert")

    override def newPhase(prev: Phase): StdPhase = new StdPhase(prev) {
      override def apply(unit: CompilationUnit): Unit = {

        val punit = unit.body.metadata("scalameta").asInstanceOf[scala.meta.Tree]

        val optionUsageInspection = new com.smartelk.scalaz.amigo.inspections.OptionUsage
        applyToInspectionContextAfterInspection(optionUsageInspection.apply(punit))
      }
    }
  }
}


