package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.BaseInspectionSpec

class MetaInspectionSpec extends BaseInspectionSpec {
  import InspectionSpecDsl._

  "test" when {

    "test" should {
      "warn expression" in {
        compile( """ List(1).toSet """) {
          _.should have inspection problem "'==' usage"
        }
      }
    }

    //List(1).toSet
  }
}
