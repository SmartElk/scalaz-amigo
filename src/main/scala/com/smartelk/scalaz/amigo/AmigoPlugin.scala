package com.smartelk.scalaz.amigo

import scala.meta.internal.hosts.scalac.{Plugin => ScalahostPlugin}
import scala.tools.nsc.plugins.{PluginComponent}
import scala.tools.nsc.{Global}


class AmigoPlugin(global: Global) extends ScalahostPlugin(global) with AmigoPhase {
  override val name: String = "scalaz-amigo"
  override val description: String = "Compiler plugin for Scalaz code-style inspections"

  override val components: List[PluginComponent] = List(ConvertComponent, AmigoComponent)

}

