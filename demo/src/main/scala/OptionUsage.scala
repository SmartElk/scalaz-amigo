import scalaz._
import Scalaz._

class OptionUsage {

  def func(opt: Option[Int]) = {
    opt.map(_ + 1)
  }

  func(1.some)

  val a = Some(6) //should warn

  func(none)

  val b = 2.some

  func(None) //should warn
}
