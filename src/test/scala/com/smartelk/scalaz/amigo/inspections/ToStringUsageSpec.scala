package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.BaseInspectionSpec

/*class ToStringUsageSpec extends BaseInspectionSpec {
  import InspectionSpecDsl._

  "Inspecting for 'toString' usage" when {

    "there are usages" should {
      "warn expression" in {
        compile( """1.toString""") {
          _.should have inspection problem "'toString' usage"
        }
      }

      "warn complex expression in def" in {
        compile( """def func = 1.toString + true.toString""") {
          _.should have inspection problems("'toString' usage", "'toString' usage")
        }
      }
    }

    "there are only scalaz 'Show' usages" should {
      "not warn them" in {
        compile(
          """import scalaz._
             import Scalaz._
             val a: String = 1.shows
             val b: String = true.shows
             val c: Cord = true.show.toString
          """) {_.should not have inspection problems }
      }
    }
  }
}*/
