package com.smartelk.scalaz.amigo

import com.smartelk.scalaz.amigo.inspections._
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.{Phase, Global}

class AmigoPlugin(val global: Global) extends Plugin {
  override val name: String = "scalaz-amigo"
  override val description: String = "Compiler plugin for Scalaz code-style inspections"

  val component = new PostTyperComponent(global)
  override val components: List[PluginComponent] = List(component)

  class PostTyperComponent(val global: Global) extends PluginComponent {
    override val phaseName: String = "scalaz-amigo-post-typer"
    override val runsAfter: List[String] = List("typer")
    override val runsBefore = List[String]("patmat")

    var applyToInspectionContextAfterInspection: (InspectionContext => Unit) = (_ => ())

    override def newPhase(prev: Phase): StdPhase = new StdPhase(prev) {
      override def apply(unit: global.CompilationUnit): Unit = {
        val context = InspectionContext(global)
        Configuration.inspections.foreach(ins => {
          val inspection = ins(context)
          inspection.traversal.traverse(unit.body.asInstanceOf[inspection.Tree])
        })
        applyToInspectionContextAfterInspection(context)
      }
    }
  }
}

object Configuration {
  val inspections = Seq(
    new OptionUsage(_),
    new EqualityUsage(_),
    new EitherUsage(_),
    new ToStringUsage(_)
  )
}

