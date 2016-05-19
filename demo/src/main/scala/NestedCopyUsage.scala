class NestedCopyUsage {

  case class A(a1: Int, a2: String)
  case class B(a: A)
  case class C(b: B, c1: Int)
  case class D(c: C)
  val obj = D(C(B(A(1, "str")), 1))
  val obj1 = obj.copy(c = obj.c.copy(b = obj.c.b.copy(a = A(2, "str1")))) //should alert

  val obj2 = obj.copy(c = obj.c.copy(c1 = 2))
}
