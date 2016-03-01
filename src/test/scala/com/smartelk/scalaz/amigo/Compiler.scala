package com.smartelk.scalaz.amigo

import scala.tools.nsc.{Global}
import tools.nsc.util.BatchSourceFile
import tools.nsc.io.{AbstractFile}
import scala.reflect.internal.util.AbstractFileClassLoader
import java.security.MessageDigest
import java.math.BigInteger
import collection.mutable

//https://github.com/twitter/util/blob/master/util-eval/src/main/scala/com/twitter/util/Eval.scala

class Compiler(target: AbstractFile, global: Global) {
  val classCache = mutable.Map[String, Class[_]]()

  private lazy val run = new global.Run

  val classLoader = new AbstractFileClassLoader(target, this.getClass.getClassLoader)

  /**Compiles the code as a class into the class loader of this compiler.
    *
    * @param code
    * @return
    */
  def compile(code: String) = {
    val className = classNameForCode(code)
    findClass(className).getOrElse {
      val codeWrappedInClass = wrapCodeInClass(className, code)
      val sourceFiles = List(new BatchSourceFile("(inline)", codeWrappedInClass))
      run.compileSources(sourceFiles)
      findClass(className).get
    }
  }

  /** Compiles the source string into the class loader and
    * evaluates it.
    *
    * @param code
    * @tparam T
    * @return
    */
  def eval[T](code: String): T = {
    val cls = compile(code)
    cls.getConstructor().newInstance().asInstanceOf[() => Any].apply().asInstanceOf[T]
  }

  def findClass(className: String): Option[Class[_]] = {
    synchronized {
      classCache.get(className).orElse {
        try {
          val cls = classLoader.loadClass(className)
          classCache(className) = cls
          Some(cls)
        } catch {
          case e: ClassNotFoundException => None
        }
      }
    }
  }

  protected def classNameForCode(code: String): String = {
    val digest = MessageDigest.getInstance("SHA-1").digest(code.getBytes)
    "sha"+new BigInteger(1, digest).toString(16)
  }

  /*
  * Wrap source code in a new class with an apply method.
  */
  private def wrapCodeInClass(className: String, code: String) = {
    "class " + className + " extends (() => Any) {\n" +
      "  def apply() = {\n" +
      code + "\n" +
      "  }\n" +
      "}\n"
  }
}
