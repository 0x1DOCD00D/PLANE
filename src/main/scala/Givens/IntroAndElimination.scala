package Givens

object IntroAndElimination:
  trait Greeting {
    def greet: String
  }

  trait PoliteGreeting extends Greeting

  given formalGreeting: Greeting = new Greeting {
    def greet: String = "Good day to you!"
  }

//  changing it to Greeting results in a type error
  given casualGreeting: PoliteGreeting = new PoliteGreeting {
    def greet: String = "Hey there!"
  }

  def sayHello(using greeting: Greeting): String = greeting.greet

  def main(args: Array[String]): Unit = {
//    println(sayHello(summon[Greeting]))
    sayHello
  }