package com.smartelk.scalaz.amigo

import scala.tools.nsc.plugins.{PluginComponent}
import scala.tools.nsc.Phase

trait AmigoPhase {
  self: AmigoPlugin =>

  object AmigoComponent extends PluginComponent {
    val global: self.global.type = self.global
    import global._

    override val phaseName: String = "scalaz-amigo-post-typer"
    override val runsAfter: List[String] = List("typer")
    override val runsBefore = List[String]("patmat")


    var applyToInspectionContextAfterInspection: (InspectionContext => Unit) = (_ => ())

    override def newPhase(prev: Phase): StdPhase = new StdPhase(prev) {
      override def apply(unit: CompilationUnit): Unit = {

        val punit = unit.body.metadata("scalameta").asInstanceOf[scala.meta.Tree]

        val context = InspectionContext(global)
        Settings.inspections.foreach(ins => {
          val inspection = ins(context)
          inspection.traversal.traverse(unit.body.asInstanceOf[inspection.Tree])
        })
        applyToInspectionContextAfterInspection(context)
      }
    }
  }
}
