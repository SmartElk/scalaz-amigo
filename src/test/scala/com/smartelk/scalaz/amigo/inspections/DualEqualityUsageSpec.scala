package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.Problems._
import com.smartelk.scalaz.amigo.BaseInspectionSpec

class DualEqualityUsageSpec  extends BaseInspectionSpec {
  import InspectionSpecDsl._

  "Inspecting for == usage" when {
    "there is == usage" should {
      "warn expression" in {
        compile( """ 1=="1" """) {
          _.should have inspection problem DualEqualityUsage
        }
      }
    }
  }
}
