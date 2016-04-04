package com.smartelk.scalaz.amigo

import scala.tools.nsc.plugins.{PluginComponent}
import scala.meta.internal.hosts.scalac.{Plugin => ScalahostPlugin}
import scala.tools.nsc._

class AmigoPlugin(g: Global) extends ScalahostPlugin(g) with AmigoPhase {
  override val components: List[PluginComponent] = List(ConvertComponent, AmigoComponent)
}

trait AmigoPhase {
  self: AmigoPlugin =>

  object AmigoComponent extends PluginComponent {
    val global: self.global.type = self.global
    import global._

    override val phaseName: String = "scalaz-amigo"
    override val runsAfter: List[String] = List("convert")

    var applyToInspectionContextAfterInspection: (InspectionContext => Unit) = (_ => ())

    override def newPhase(prev: Phase): StdPhase = new StdPhase(prev) {
      override def apply(unit: CompilationUnit): Unit = {

        val punit = unit.body.metadata("scalameta").asInstanceOf[scala.meta.Tree]

        val metaInspection = new com.smartelk.scalaz.amigo.inspections.MetaInspection

        val test = metaInspection.apply(punit)

        val asd = 1
      }
    }
  }
}