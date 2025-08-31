package Cats

object SemigroupK_v1 {
  def main(args: Array[String]): Unit = {
    import cats.SemigroupK
    import cats.instances.option._ // for SemigroupK
    import cats.instances.list._   // for SemigroupK

    val opt1: Option[Int] = Some(1)
    val opt2: Option[Int] = Some(2)
    val optNone: Option[Int] = None

    val optionSemigroupK = SemigroupK[Option]

    // Combine two Some values
    val combinedSome: Option[Int] = optionSemigroupK.combineK(opt1, opt2)
    println(s"Combined Some(1) and Some(2): $combinedSome") // Output: Some(1)

    // Combine Some and None
    val combinedWithNone1: Option[Int] = optionSemigroupK.combineK(opt1, optNone)
    println(s"Combined Some(1) and None: $combinedWithNone1") // Output: Some(1)

    val combinedWithNone2: Option[Int] = optionSemigroupK.combineK(optNone, opt2)
    println(s"Combined None and Some(2): $combinedWithNone2") // Output: Some(2)

    // Combine two None values
    val combinedNone: Option[Int] = optionSemigroupK.combineK(optNone, optNone)
    println(s"Combined None and None: $combinedNone") // Output: None

    // Now for List
    val list1: List[Int] = List(1, 2)
    val list2: List[Int] = List(3, 4)

    val listSemigroupK = SemigroupK[List]

    // Combine two lists
    val combinedList: List[Int] = listSemigroupK.combineK(list1, list2)
    println(s"Combined List(1, 2) and List(3, 4): $combinedList") // Output: List(1, 2, 3, 4)
  }
}
