package com.smartelk.scalaz.amigo.inspections

import com.smartelk.scalaz.amigo._

class EitherUsageSpec extends BaseInspectionSpec {
  import InspectionSpecDsl._

  "Inspecting for 'scala.util.Either' usage" when {

    "there is usage of 'Right'" should {
      "warn expression" in {
        compile( """Right(123)""") {
          _.should have inspection problem "'Right' usage"
        }
      }
      "warn in val" in {
        compile( """val a: Either[String, Int] = Right(123)""") {
          _.should have inspection problem "'Right' usage"
        }
      }
      "warn in def" in {
        compile( """def func = Right("bla")""") {
          _.should have inspection problem "'Right' usage"
        }
      }
      "warn complex expression in def" in {
        compile( """def func = Right(123).right.get + Right(124).right.get""") {
          _.should have inspection problems("'Right' usage", "'Right' usage")
        }
      }
    }

    "there is usage of 'Left'" should {
      "warn expression" in {
        compile( """Left("bla")""") {
          _.should have inspection problem "'Left' usage"
        }
      }
      "warn in val" in {
        compile( """val a: Either[String, Int] = Left("bla")""") {
          _.should have inspection problem "'Left' usage"
        }
      }
      "warn in def" in {
        compile( """def func = Left("bla")""") {
          _.should have inspection problem "'Left' usage"
        }
      }
      "warn complex expression in def" in {
        compile( """def func = Left("bla").left.get + Left("bla").left.get""") {
          _.should have inspection problems("'Left' usage", "'Left' usage")
        }
      }
    }

    "there are multiple 'Right' and 'Left' usages" should {
      "warn them all" in {
        compile( """def func: Either[Int, String] = {val a = Left(1); Right("blab") }""") {
          _.should have inspection problems("'Left' usage", "'Right' usage")
        }
      }
    }

    "there are only scalaz usages" should {
      "not warn them" in {
        compile(
          """import scalaz._
             import Scalaz._
             val a: \/[String,Int] = 123.right[String]
             val b: \/[String,Int] = "bla".left[Int]""") {_.should not have inspection problems }
      }
    }
  }
}