package com.smartelk.scalaz.amigo

import scala.reflect.io.VirtualDirectory
import scala.tools.nsc.{Global}
import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.internal.util.AbstractFileClassLoader
import java.security.MessageDigest
import java.math.BigInteger

class Compiler(targetDir: VirtualDirectory, global: Global) {
  private lazy val run = new global.Run
  private val classLoader = new AbstractFileClassLoader(targetDir, this.getClass.getClassLoader)

  def compile(code: String): Class[_] = {
    val className = classNameForCode(code)
    val codeWrappedInClass = wrapCodeInClass(className, code)
    val sourceFiles = List(new BatchSourceFile("(inline)", codeWrappedInClass))
    run.compileSources(sourceFiles)
    classLoader.loadClass(className)
  }

  private def classNameForCode(code: String): String = {
    val digest = MessageDigest.getInstance("SHA-1").digest(code.getBytes)
    "sha"+new BigInteger(1, digest).toString(16)
  }

  private def wrapCodeInClass(className: String, code: String) = {
    "class " + className + " extends (() => Any) {\n" +
      "  def apply() = {\n" +
      code + "\n" +
      "  }\n" +
      "}\n"
  }
}
