package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.Problems._
import com.smartelk.scalaz.amigo._

class ScalaOptionUseSpec extends BaseInspectionSpec {

  "Inspecting Option" when {
    "there is usage of Some" should {
      "warn Some expression" in {
        compile("""Some(123)""") {_.should have inspection problems ScalaSomeUsage }
      }
      "warn Some variable" in {
        compile("""val a: Option[String] = Some("123")""") {_.should have inspection problems ScalaSomeUsage}
      }
      "warn Some in def" in {
        compile("""def func = Some(123)""") {_.should have inspection problems ScalaSomeUsage}
      }
    }
  }

  "there is usage of None" should {
    "warn None variable" ignore {
      compile( """val a: Option[String] = None""") {_.should have inspection problems ScalaSomeUsage}
    }
    "warn None in def" ignore {
      compile("""def func: Option[Int] = None""") {_.should have inspection problems ScalaNoneUsage}
    }
  }
}