package com.smartelk.scalaz.amigo

import com.smartelk.scalaz.amigo.inspections.{ToStringUsage, EqualityUsage, EitherUsage, OptionUsage}
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc._
import scala.meta._

class AmigoPlugin(val global: Global) extends Plugin {
  val self = this

  override val components: List[PluginComponent] = List(AmigoComponent)
  val name = "scalaz-amigo"
  val description = "scalaz-amigo"

  var applyToInspectionContextAfterInspection: (Seq[Warning] => Unit) = (_ => ())

  object AmigoComponent extends PluginComponent {
    override val global: self.global.type = self.global

    override val phaseName: String = "scalaz-amigo"
    override val runsAfter: List[String] = List("typer")

    override def newPhase(prev: Phase): StdPhase = new StdPhase(prev) {
      override def apply(unit: global.CompilationUnit): Unit = {
        val mtree = unit.source.content.parse[Source]
        val result = inspections.flatMap(_(mtree))
        applyToInspectionContextAfterInspection(result)
      }
    }
  }

  val inspections = Seq(
    new OptionUsage,
    new EitherUsage,
    new EqualityUsage,
    new ToStringUsage
  )
}


