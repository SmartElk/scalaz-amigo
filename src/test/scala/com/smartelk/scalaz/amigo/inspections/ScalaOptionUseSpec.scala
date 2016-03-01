package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo.{Warning, BaseInspectionSpec}

class ScalaOptionUseSpec extends BaseInspectionSpec {

  "Inspecting Option" when {
    "there is usage of Some" should {
      "warn" in {
        val warning = Warning("""Using Scala's "Some" directly""", """Use Scalaz's "some"""")
        compile("""Some(123)""") {_.problems should be (Seq(warning))}
        compile( """val a: Option[String] = Some("123")""") {_.problems should be (Seq(warning))}
        compile( """def func = Some(123)""") {_.problems should be (Seq(warning))}
      }
    }

    "there is usage of None" should {
      "warn" ignore {
        compile( """val a: Option[String] = None """) { p =>
          p.problems should be (Seq(Warning("""Using Scala's "None" directly""", """Use Scalaz's "none"""")))
        }
      }
    }
  }
}