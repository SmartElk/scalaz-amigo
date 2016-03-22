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
             val c: Option[Int] = 1.some""") {_.should not have inspection problems }
        }
    }
  }
}