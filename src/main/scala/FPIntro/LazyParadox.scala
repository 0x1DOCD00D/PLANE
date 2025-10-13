package FPIntro

object LazyParadox {
  lazy val x: Int = {
    throw new Exception("ouch!!!")
    1
  }

  lazy val y: Int = {
    println("executing y")
    1
  }
/*
1. x is a lazy val, so its body runs on the first access.
2. Inside main, the first LazyParadox.x runs inside a try.
Evaluating `x` executes the block, which immediately `throw`s `new Exception("ouch!!!")`.
The `catch case _ => println("got it!")` catches it and prints `got it!`.
Because initialization failed, the lazy val is not considered initialized.
Scala only memoizes a lazy value when its initialization completes successfully.
3. The next line accesses `LazyParadox.x` again,
this time outside any `try`. Since the earlier attempt failed, Scala tries to initialize `x`
again, hits the same `throw`, and this exception is uncaught.
Program terminates with `Exception: ouch!!!`.

lazy vals cache only successful results, not failed initializations.
If you want to avoid the crash here, either wrap the second access in a `try/catch`,
or change the definition to return a safe container like `Try`/`Either` and handle it explicitly.

* */
  def main(args: Array[String]): Unit = {
    try {
      LazyParadox.y
      LazyParadox.x
    } catch
      case _ => println("got it!")
    LazyParadox.y
    LazyParadox.x
  }
}
