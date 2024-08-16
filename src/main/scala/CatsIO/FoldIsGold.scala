package CatsIO

import CatsIO.Aid4Debugging.printStackContentEagerly
import cats.effect.{ExitCode, IO, IOApp}
import cats.{Eval, Foldable}
import cats.instances.list.*
import cats.instances.lazyList.*

object FoldIsGold extends IOApp:
  def failWithStackoverflow(howmany: Int): Int =
    val listOfFuncs = List.fill(howmany)((x:Int)=>x+1)
    val composedF = listOfFuncs.foldLeft((x:Int)=>x)((acc, elem)=> (y:Int) =>acc(elem(y)))
    composedF(1)

  def stillFails(howmany: Int): Int =
    val listOfFuncs = List.fill(howmany)((x:Int)=>x+1)
    val composedF = Foldable[List].foldLeft(listOfFuncs, (x:Int)=>x)((acc, elem)=> (y:Int) =>acc(elem(y)))
    composedF(1)

  def tutorialExample(howmany: Int): Long =
    def bigData = (1 to howmany).to(LazyList)
    bigData.foldRight(0L)(_ + _)

  def tutorialExample1(howmany: Int): Long =
    def bigData = (1 to howmany).to(List)

    bigData.foldRight(0L)(_ + _)

  def worksWithEvalButStillFails(howmany: Int): Int =
    val listOfFuncs = LazyList.fill(howmany)((x:Int)=>x+1)
    val eval: Eval[Int=>Int] = Foldable[LazyList].foldLeft(listOfFuncs, Eval.later((x:Int)=>x)) { (acc, elem) => Eval.later(acc.value.compose(elem)) }
    eval.value(1)

  def withFoldLeftMap(howmany: Int): Int =
    val listOfFuncs = LazyList.fill(howmany)((x:Int)=>Eval.now(x+1))
    val eval = Foldable[LazyList].foldLeft(listOfFuncs, (x:Int)=>Eval.now(x)) { (acc, elem) => i => acc(i).flatMap(elem) }
    eval(1).value

  def withFoldRightMap(howmany: Int): Int =
    val listOfFuncs = LazyList.fill(howmany)((x:Int)=>x+1)
    val eval = Foldable[LazyList].foldRight(listOfFuncs, Eval.now((x:Int)=>x)) { (acc, elem) => Eval.later(acc.compose(elem.value)) }
    eval.value(1)

  def withFoldRunsFine(howmany: Int, start:Int): Int =
    val listOfFuncs = LazyList.fill(howmany)((x:Int)=>x+1)
    Foldable[LazyList].foldLeft(listOfFuncs, start) { (acc, elem) => elem(acc) }

  override def run(args: List[String]): IO[ExitCode] =
    IO.println(withFoldRunsFine(10000000,1)) >> IO.unit.as(ExitCode.Success)
//    IO.println(withFoldRightMap(1000)) >> IO.unit.as(ExitCode.Success)
//    IO.println(withFoldLeftMap(1000)) >> IO.unit.as(ExitCode.Success)
    //IO.println(tutorialExample1(1000000)) >> IO.unit.as(ExitCode.Success)
    //IO.println(worksWithEvalButStillFails(1000)) >> IO.unit.as(ExitCode.Success)
    //IO.println(failWithStackoverflow(1000000)) >> IO.unit.as(ExitCode.Success)
    //IO.println(stillFails(1000000)) >> IO.unit.as(ExitCode.Success)
