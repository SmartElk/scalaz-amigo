package com.smartelk.scalaz.amigo

import org.scalatest.{WordSpecLike, Matchers}
import scala.collection.mutable
import scala.reflect.io.VirtualDirectory
import scala.tools.nsc.{Settings, Global}

trait BaseInspectionSpec extends WordSpecLike with Matchers {

  object Compiler {
    val target =  new VirtualDirectory("(memory)", None)

    private val settings = new Settings()
    //settings.deprecation.value = true // enable detailed deprecation warnings
    //settings.unchecked.value = true // enable detailed unchecked warnings
    settings.outputDirs.setSingleOutput(target)
    settings.usejavacp.value = true

    val global = new Global(settings)
    val components = new AmigoPlugin(global).components
    for (phase <- components) {
      import scala.language.reflectiveCalls
      global.asInstanceOf[{def phasesSet: mutable.HashSet[tools.nsc.SubComponent]}].phasesSet += phase
    }
    val compiler = new Compiler(target, global)
  }

  def compile(code: String)(assert: InspectionContext => Unit): Unit = {
    Compiler.components.foreach(c => {
      import scala.language.reflectiveCalls
      c.asInstanceOf[{var applyToInspectionContextAfterInspection: InspectionContext => Unit}].applyToInspectionContextAfterInspection = assert
    })
    Compiler.compiler.compile(code)
  }
}
