package com.smartelk.scalaz.amigo

import org.scalatest.{WordSpecLike, Matchers}
import scala.tools.nsc.Global

trait BaseInspectionSpec extends WordSpecLike with Matchers {

  def compile(code: String)(assert: Seq[FoundProblem] => Unit): Unit = {
    val pluginComponents = (g: Global) =>  new AmigoPlugin(g){
      override def runAfter(problems: Seq[FoundProblem]): Unit = assert(problems)
    }.components
    val compiler = new Compiler(None, pluginComponents)
    compiler.compile(code)
  }
}
