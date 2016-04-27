package com.smartelk.scalaz.amigo

import java.math.BigInteger
import java.security.MessageDigest
import org.scalatest.words.{HaveWord}
import org.scalatest.{WordSpecLike, Matchers}
import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.io.{File, VirtualDirectory}
import scala.tools.nsc.{Settings, Global}

trait BaseInspectionSpec extends WordSpecLike with Matchers {
  val dependencies = List(
    "org.scala-lang/scala-library/jars/scala-library-2.11.8.jar",
    "org.scalaz/scalaz-core_2.11/jars/scalaz-core_2.11-7.2.2.jar"
  )

  def compile(code: String)(assert: Seq[WarningWithContext] => Unit): Unit = {
    val targetDir =  new VirtualDirectory("(memory)", None)
    var pluginCalled = false

    val settings = {
      val settings = new Settings()
      settings.outputDirs.setSingleOutput(targetDir)
      //settings.stopAfter.value = List("refchecks")

      val homePath = System.getProperty("user.home")
      val classPath = dependencies.map(homePath + "\\.ivy2\\cache\\" + _)
      settings.classpath.value = classPath.mkString(File.pathSeparator)
      settings
    }

    val global = {
      val global = new Global(settings)
      val _ = global.plugins

      val f_plugins = global.getClass.getDeclaredField("plugins")
      f_plugins.setAccessible(true)

      val amigoPlugin = new AmigoPlugin(global) {
        override def applyOnInspectionResult(warnings: Seq[WarningWithContext]) = {
          pluginCalled = true
          assert(warnings)
        }
      }
      f_plugins.set(global, global.plugins :+ amigoPlugin)
      global
    }

    def compileCode(code: String) = {
      val run = new global.Run
      //private val classLoader = new AbstractFileClassLoader(targetDir, this.getClass.getClassLoader)

      val className = {
        val digest = MessageDigest.getInstance("SHA-1").digest(code.getBytes)
        "sha" + new BigInteger(1, digest).toString(16)
      }

      val codeWrappedInClass = {
        "class " + className + "{" +
          code +
          "}"
      }

      val sourceFiles = List(new BatchSourceFile("(inline)", codeWrappedInClass))
      run.compileSources(sourceFiles)
      //classLoader.loadClass(className) //no need to load it, only compilation with plugin matters for us
    }

    compileCode(code)

    Predef.assert(pluginCalled, "Amigo plugin has not been called! Problem with the configuration?")
  }

  object InspectionSpecDsl {
    object inspection
    object problems
    implicit class InspectionResultWrapper(warnings: Seq[WarningWithContext]){
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
        warnings.map(_.warning.problem) should be (problems)
      }
    }
  }
}

