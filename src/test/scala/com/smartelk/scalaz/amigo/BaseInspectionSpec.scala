package com.smartelk.scalaz.amigo

import org.scalatest.{WordSpecLike, Matchers}
import scala.reflect.io.VirtualDirectory
import scala.tools.nsc.{Settings, Global}
import scala.collection.mutable

trait BaseInspectionSpec extends WordSpecLike with Matchers {

  def compile(code: String)(assert: InspectionContext => Unit): Unit = {
    val targetDir =  new VirtualDirectory("(memory)", None)

    val settings = new Settings()
    //settings.deprecation.value = true // enable detailed deprecation warnings
    //settings.unchecked.value = true // enable detailed unchecked warnings
    settings.outputDirs.setSingleOutput(targetDir)
    settings.usejavacp.value = true

    val global = new Global(settings)

    val components = new AmigoPlugin(global).components
    components.foreach(c => {
      import scala.language.reflectiveCalls
      c.asInstanceOf[{var applyToInspectionContextAfterInspection: InspectionContext => Unit}].applyToInspectionContextAfterInspection = assert
    })

    for (phase <- components) {
      import scala.language.reflectiveCalls
      global.asInstanceOf[{def phasesSet: mutable.HashSet[tools.nsc.SubComponent]}].phasesSet += phase
    }

    val compiler = new Compiler(targetDir, global)
    compiler.compile(code)
  }

  object inspection
  implicit class ContextWrapper(context: InspectionContext){
    def should = new ShouldAction

    class ShouldAction {
      def have(word: inspection.type) = new HaveAction
    }
    class HaveAction {
      def problems(problems: Problem*) = {
        shouldHaveInspectionProblems(problems:_*)
      }
    }

    def shouldHaveInspectionProblems(problems: Problem*) = {
      context.messages.map(_.problem) should be (problems)
    }
  }
}
