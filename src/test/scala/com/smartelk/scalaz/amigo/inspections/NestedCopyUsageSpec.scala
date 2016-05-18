package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.BaseInspectionSpec

class NestedCopyUsageSpec extends BaseInspectionSpec {
  import InspectionSpecDsl._

  "Inspecting for nested 'copy' usage" when {

    "there is a nested usage" should {
      "warn" in {
        compile(
          """
            case class A(a1: Int, a2: String)
            case class B(a: A)
            case class C(b: B)
            case class D(c: C)
            val obj = D(C(B(A(1, "str"))))
            val updated = obj.copy(c = obj.c.copy(b = obj.c.b.copy(a = A(2, "str1"))))
          """) {
          _.should have inspection problem "'Nested copy' usage"
        }
      }
    }

    "there is a nested usage (but nesting is not so deep to be warned)" should {
      "not warn" in {
        compile(
          """
            case class A(a1: Int, a2: String)
            case class B(a: A)
            case class C(b: B)
            val obj = C(B(A(1, "str")))
            val updated = obj.copy(b = obj.b.copy(a = A(2, "str1")))
          """) {
          _.should not have inspection problems
        }
      }
    }

    "there are no usages" should {
      "not warn" in {
        compile(
          """
            case class A(a1: Int, a2: String)
            case class B(a: A)
            case class C(b: B)
            val obj = C(B(A(1, "str")))
          """) {
          _.should not have inspection problems
        }
      }
    }
  }
}
