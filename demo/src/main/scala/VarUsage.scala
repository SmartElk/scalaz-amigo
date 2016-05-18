abstract class VarUsage {
  val a = 1
  var b = 2 //should alert

  var c: Int //should alert

  def func() = {
    var b = 3 //should alert
    b = 4
    b + 1
  }
}
