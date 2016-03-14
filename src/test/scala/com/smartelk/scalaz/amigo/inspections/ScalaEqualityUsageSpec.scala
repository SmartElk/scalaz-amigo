package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.Problems._
import com.smartelk.scalaz.amigo.BaseInspectionSpec

class ScalaEqualityUsageSpec  extends BaseInspectionSpec {
  import InspectionSpecDsl._

  "Inspecting for == usage" when {

    "there is == usage comparing different types" should {
      "warn expression" in {
        compile(""" 1 == "1" """) {
          _.should have inspection problem ScalaEqualityUsage("==")
        }
      }
    }

    "there is == usage comparing object types" should {
      "warn expression" in {
        compile(
          """case class Test(t1: String)
             val a = Test("a")
             val b = Test("b")
             a == b """) {
          _.should have inspection problem ScalaEqualityUsage("==")
        }
      }
    }

    "there is == usage comparing numeric types" should {
      "warn expression" in {
        compile(
          """
            def test1: Int = 1
            1 == test1
            """.stripMargin) {
          _.should have inspection problem ScalaEqualityUsage("==")
        }
      }
    }

    "there are no == usages" should {
      "not warn" in {
        compile(
          """import scalaz._
             import Scalaz._
             1 === 1 """) {
          _.should not have inspection problems
        }
      }
    }
  }

  "Inspecting for != usage" when {

    "there is != usage comparing different types" should {
      "warn expression" in {
        compile(""" 1 != "2" """) {
          _.should have inspection problem ScalaEqualityUsage("!=")
        }
      }
    }

    "there is != usage comparing object types" should {
      "warn expression" in {
        compile(
          """case class Test(t1: String)
             val a = Test("a")
             val b = Test("b")
             a != b """) {
          _.should have inspection problem ScalaEqualityUsage("!=")
        }
      }
    }

    "there is != usage comparing numeric types" should {
      "warn expression" in {
        compile(
          """
            def test1: Int = 1
            1 != test1
          """.stripMargin) {
          _.should have inspection problem ScalaEqualityUsage("!=")
        }
      }
    }

    "there are no != usages" should {
      "not warn" in {
        compile(
          """import scalaz._
             import Scalaz._
             1 =/= 1 """) {
          _.should not have inspection problems
        }
      }
    }
  }
}
