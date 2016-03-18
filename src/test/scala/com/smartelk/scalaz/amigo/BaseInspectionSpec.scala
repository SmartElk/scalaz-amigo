package com.smartelk.scalaz.amigo

import org.scalatest.words.{HaveWord}
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

  object InspectionSpecDsl {
    object inspection
    object problems
    implicit class ContextWrapper(context: InspectionContext){
      def should = new ShouldAction

      class ShouldAction {
        def not (haveWord: HaveWord) = new ProblemsAction
        def have(inspectionWord: inspection.type) = new HaveAction
      }

      class HaveAction {
        def problems(problems: String*) = shouldHaveInspectionProblems(problems:_*)
        def problem(problem: String) = shouldHaveInspectionProblems(problem)
      }

      class ProblemsAction {
        def inspection(problemsWord: problems.type) = shouldHaveInspectionProblems()
      }

      def shouldHaveInspectionProblems(problems: String*) = {
        context.messages.map(_.problem) should be (problems)
      }
    }
  }
}

