package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.Problems._
import com.smartelk.scalaz.amigo._

class ScalaOptionUsageSpec extends BaseInspectionSpec {
  import InspectionSpecDsl._

  "Inspecting for scala.Option usage" when {

    "there is usage of Some" should {
      "warn Some expression" in {
        compile( """Some(123)""") {
          _.should have inspection problem ScalaOptionUsage("Some")
        }
      }
      "warn Some in val" in {
        compile( """val a: Option[String] = Some("123")""") {
          _.should have inspection problem ScalaOptionUsage("Some")
        }
      }
      "warn Some in def" in {
        compile( """def func = Some(123)""") {
          _.should have inspection problem ScalaOptionUsage("Some")
        }
      }
      "warn complex Some expression in def" in {
        compile( """def func = Some(123).flatMap(a => Some(a + 1))""") {
          _.should have inspection problems(ScalaOptionUsage("Some"), ScalaOptionUsage("Some"))
        }
      }
    }

    "there is usage of None" should {
      "warn None in val" in {
        compile( """val a: Option[String] = None""") {
          _.should have inspection problem ScalaOptionUsage("None")
        }
      }
      "warn None in def" in {
        compile( """def func: Option[Int] = None""") {
          _.should have inspection problem ScalaOptionUsage("None")
        }
      }
    }

    "there are multiple Some and None usages" should {
      "warn them all" in {
        compile( """def func: Option[Int] = {val a = Some(1); None }""") {
          _.should have inspection problems(ScalaOptionUsage("Some"), ScalaOptionUsage("None"))
        }
      }
    }

    "there are only scalaz Option usages" should {
      "not warn them" in {
        compile(
          """import scalaz._
             import Scalaz._
             val a: Option[String] = none
             val b = none[Int]
             val c = 1.some""") {_.should not have inspection problems }
        }
    }
  }
}