package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo._

class OptionUsageSpec extends BaseInspectionSpec {
  import InspectionSpecDsl._

  "Inspecting for 'scala.Option' usage" when {

    "there is usage of 'Some'" should {
      "warn expression" in {
        compile( """Some(123)""") {
          _.should have inspection problem "'Some' usage"
        }
      }
      "warn in val" in {
        compile( """val a: Option[String] = Some("123")""") {
          _.should have inspection problem "'Some' usage"
        }
      }
      "warn in def" in {
        compile( """def func = Some(123)""") {
          _.should have inspection problem "'Some' usage"
        }
      }
      "warn complex expression in def" in {
        compile( """def func = Some(123).flatMap(a => Some(a + 1))""") {
          _.should have inspection problems("'Some' usage", "'Some' usage")
        }
      }
    }

    "there is usage of 'None'" should {
      "warn in val" in {
        compile( """val a: Option[String] = None""") {
          _.should have inspection problem "'None' usage"
        }
      }
      "warn in def" in {
        compile( """def func: Option[Int] = None""") {
          _.should have inspection problem "'None' usage"
        }
      }
    }

    "there are multiple 'Some' and 'None' usages" should {
      "warn them all" in {
        compile( """def func: Option[Int] = {val a = Some(1); None }""") {
          _.should have inspection problems("'Some' usage", "'None' usage")
        }
      }
    }

    "there are only scalaz 'Option' usages" should {
      "not warn them" in {
        compile(
          """import scalaz._
             import Scalaz._
             val a: Option[String] = none
             val b: Option[Int] = none[Int]
             val c: Option[Int] = 1.some""") {
          _.should not have inspection problems
        }
      }
    }

    //todo: false positive. Probably we need to use scalameta's semantic analysis to fix it
    "there are Option usages but not Scala's standard ones" should {
      "not warn custom Some" ignore {
        compile(
          """object Custom {
               case class Some(a: Int)
               val a: Custom.Some = Some(1)
              }""") {
          _.should not have inspection problems
        }
      }

      "not warn custom None" ignore {
        compile(
          """object Custom {
                case object None
                val a: Custom.None.type = None
              }""") {
          _.should not have inspection problems
        }
      }
    }
  }
}