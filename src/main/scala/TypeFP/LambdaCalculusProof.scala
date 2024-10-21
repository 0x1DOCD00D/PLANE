package TypeFP

//copied from https://gist.github.com/RaasAhsan/ccb85e26346c1b158fc9a69b5ab33820

object LambdaCalculusProof:
  sealed trait Term
  sealed trait Var[I <: Index] extends Term
  sealed trait App[T1 <: Term, T2 <: Term] extends Term
  sealed trait Abs[T1 <: Term] extends Term
  
  sealed trait If[T1 <: Term, T2 <: Term, T3 <: Term] extends Term
  sealed trait Bool[T1 <: Boolean] extends Term
  
  sealed trait Index
  sealed trait IZ extends Index
  sealed trait IS[Prev <: Index] extends Index
  
  sealed trait Env
  sealed trait EZ extends Env
  sealed trait ES[T <: Term, E <: Env] extends Env
  
  type lookup[I <: Index, E <: Env] = (I, E) match {
    case (IZ, ES[t, _]) => t
    case (IS[i], ES[t, e]) => lookup[i, e]
    case _ => Nothing
  }
  
  type eval0[T <: Term, E <: Env] = T match {
    case Var[i] => lookup[i, E]
    case App[t1, t2] => eval0[t1, E] match {
      case Abs[t] => eval0[t, ES[eval0[t2, E], E]]
      case _ => Nothing
    }
    case If[t1, t2, t3] => eval0[t1, E] match {
      case Bool[b] => b match {
        case true => eval0[t2, E]
        case false => eval0[t3, E]
      }
      case _ => Nothing
    }
    case _ => T
  }
  
  type eval[T <: Term] = eval0[T, EZ]
  
  type t1 = App[Abs[Var[IZ]], Bool[true]]
  summon[eval[t1] =:= Bool[true]]
  
  type t2 = If[App[Abs[Var[IZ]], Bool[true]], Bool[false], Bool[true]]
  summon[eval[t2] =:= Bool[false]]
  
  // terms that are "stuck" evaluate to Nothing
  type t3 = If[Abs[Var[IZ]], Bool[true], Bool[false]]
  summon[eval[t3] =:= Nothing]
  
  // type checking is undecidable!
//  type omega = App[Abs[App[Var[Z],Var[Z]]], Abs[App[Var[Z],Var[Z]]]]
  def main(args: Array[String]): Unit = {
  
  }