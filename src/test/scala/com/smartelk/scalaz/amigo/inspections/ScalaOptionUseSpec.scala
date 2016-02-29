package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.BaseInspectionSpec

class ScalaOptionUseSpec extends BaseInspectionSpec {

  "Playing" when {
    "playing" should {
      "play" in {
        compile( """Some(123)""") { p =>
          p should be (Seq())
        }
      }
    }
  }
}
