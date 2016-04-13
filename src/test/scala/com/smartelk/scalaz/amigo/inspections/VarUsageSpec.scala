package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.BaseInspectionSpec

class VarUsageSpec extends BaseInspectionSpec {
  import InspectionSpecDsl._

  "Inspecting for 'var' usage" when {

    "there are usages" should {
      "warn" in {
        compile("""var b: Int = 1""") {
          _.should have inspection problem "'var' usage"
        }
      }

      "warn in def" in {
        compile(
          """def func = {
                var b = "123"
             }""") {
          _.should have inspection problems("'var' usage")
        }
      }

      "warn in class" in {
        compile(
          """
            class A {
              var a = 123
            }
          """) {
          _.should have inspection problem "'var' usage"
        }
      }

      "warn abstract declaration in class" in {
        compile(
          """
            abstract class A {
              var a: Int
            }
          """) {
          _.should have inspection problem "'var' usage"
        }
      }
    }
  }
}
