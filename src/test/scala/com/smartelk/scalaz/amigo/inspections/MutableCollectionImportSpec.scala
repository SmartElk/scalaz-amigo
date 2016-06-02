package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.BaseInspectionSpec

class MutableCollectionImportSpec extends BaseInspectionSpec {
  import InspectionSpecDsl._

  "Inspecting for mutable collection import" when {

    "there is ArrayBuffer usage" should {
      "warn" in {
        compile("""
            import scala.collection.mutable._,scala.collection
            val col = ArrayBuffer.empty[Int]
                """) {
          _.should have inspection problem "Mutable collection import"
        }
      }
    }
  }
}
