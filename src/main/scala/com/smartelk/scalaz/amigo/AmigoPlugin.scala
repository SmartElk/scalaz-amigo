package com.smartelk.scalaz.amigo

import scala.meta.internal.hosts.scalac.contexts.Adapter
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc._
import scala.{Seq => _}

class AmigoPlugin(val global: Global) extends Plugin {
  val self = this

  override val components: List[PluginComponent] = List(AmigoComponent)
  val name = "scalaz-amigo"
  val description = "scalaz-amigo"

  //var applyToInspectionContextAfterInspection: (Seq[Warning] => Unit) = (_ => ())

  object AmigoComponent extends PluginComponent {
    override val global: self.global.type = self.global
    import global._

    val adapter = new Adapter[global.type](global)

    override val phaseName: String = "scalaz-amigo"
    override val runsAfter: List[String] = List("typer")

    override def newPhase(prev: Phase): StdPhase = new StdPhase(prev) {
      override def apply(unit: CompilationUnit): Unit = {

        val mtree = adapter.toMtree(unit.body)

        val optionUsageInspection = new com.smartelk.scalaz.amigo.inspections.OptionUsage
        //applyToInspectionContextAfterInspection(optionUsageInspection.apply(punit))
      }
    }
  }
}


