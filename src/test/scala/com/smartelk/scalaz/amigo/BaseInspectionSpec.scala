package com.smartelk.scalaz.amigo

import org.scalatest.words.{HaveWord}
import org.scalatest.{WordSpecLike, Matchers}
import scala.reflect.io.VirtualDirectory
import scala.tools.nsc.{Settings, Global}

trait BaseInspectionSpec extends WordSpecLike with Matchers {

  def compile(code: String)(assert: Seq[Warning] => Unit): Unit = {
    val targetDir =  new VirtualDirectory("(memory)", None)

    val settings = new Settings()
    //settings.deprecation.value = true // enable detailed deprecation warnings
    //settings.unchecked.value = true // enable detailed unchecked warnings
    settings.outputDirs.setSingleOutput(targetDir)
    settings.usejavacp.value = true

    val global = new Global(settings)
    val _ = global.plugins

    val f_plugins = global.getClass.getDeclaredField("plugins")
    f_plugins.setAccessible(true)
    val amigoPlugin = new AmigoPlugin(global)
    f_plugins.set(global, global.plugins :+ amigoPlugin)

    amigoPlugin.applyToInspectionContextAfterInspection = assert

    val compiler = new Compiler(targetDir, global)
    compiler.compile(code)
  }

  object InspectionSpecDsl {
    object inspection
    object problems
    implicit class ContextWrapper(context: Seq[Warning]){
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
        context.map(_.problem) should be (problems)
      }
    }
  }
}

