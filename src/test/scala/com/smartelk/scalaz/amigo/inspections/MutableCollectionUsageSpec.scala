package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.BaseInspectionSpec

class MutableCollectionUsageSpec extends BaseInspectionSpec {
  import InspectionSpecDsl._

  //todo: also test for 'one-under-another type of imports'

  "Inspecting for mutable collection usage" when {

    "there is 'scala.collection.mutable' usage" should {
      "warn" in {
        compile("""
            val col = scala.collection.mutable.ArrayBuffer.empty[Int]
          """) {
          _.should have inspection problem "Mutable collection usage"
        }
      }
    }

    "there is 'collection.mutable' usage" should {
      "warn" ignore {
        compile("""
            import scala._
            val col = collection.mutable.ArrayBuffer.empty[Int]
                """) {
          _.should have inspection problem "Mutable collection usage"
        }
      }
    }

    "there is 'mutable' usage" should {
      "warn" ignore {
        compile("""
            import scala.collection._
            val col = mutable.ArrayBuffer.empty[Int]
                """) {
          _.should have inspection problem "Mutable collection usage"
        }
      }
    }

    "there is ArrayBuffer usage" should {
      "warn" ignore {
        compile("""
            import scala.collection.mutable._
            val col = ArrayBuffer.empty[Int]
                """) {
          _.should have inspection problem "Mutable collection usage"
        }
      }
    }
  }
}
