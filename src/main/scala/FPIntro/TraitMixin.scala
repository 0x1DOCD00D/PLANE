package FPIntro

object TraitMixin {

  class Professor //extends Student
  class Student
  trait Glasses:
    self: Student =>
    def sees = println("Rushi can see and walk!")

  trait Experiment:
    trait Bhumika:
      trait Mark:
        def teaches = println("Mark's teaching sucks!!!")

  class X extends Experiment:
    new Bhumika {
      new Mark {}.teaches
    }
//  class Bespectacled extends Student with Glasses:
//    def sees = println("Rushi can see and walk!")

  def main(args: Array[String]): Unit = {
    val rushi = new Student with Glasses with Experiment {}
//    val mark = new Professor with Glasses {}
    rushi.sees
    (new X)
  }
}
