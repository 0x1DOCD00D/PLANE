package Cats

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

import cats.data.State


object StateMachines:
  given Map[String, Int] = Map("a"-> 1, "b"->2)
  enum ExpressionProblem:
    case Value(v:Int)
    case Variable(name:String)
    case Add(op1: ExpressionProblem, op2: ExpressionProblem)
    case Sub(op1: ExpressionProblem, op2: ExpressionProblem)

    def eval(using envTable:Map[String,Int]):Int = this match
      case Value(v) => v
      case Variable(name) => envTable.getOrElse(name, throw new Exception(s"variable $name is not defined"))
      case Add(op1,op2)=> op1.eval + op2.eval
      case Sub(op1,op2)=> op1.eval - op2.eval

  val initState: State[ExpressionProblem, Int] = State[ExpressionProblem, Int] { state =>
    (state, state.eval)
  }

  val state_add_10: State[ExpressionProblem, Int] = State[ExpressionProblem, Int] { state =>
    import ExpressionProblem.*
    (Add(state, Value(10)), Add(state, Value(10)).eval)
  }

  @main def runMain_StateMachines$(): Unit =
    import ExpressionProblem.*
    println(Add(Add(Variable("a"), Value(10)), Sub(Value(2), Variable("b"))).eval)
    val state = initState.run(Add(Add(Variable("a"), Value(10)), Sub(Value(2), Variable("b")))).value
    println(state)
    val transition1:State[ExpressionProblem, Int] = State((ep:ExpressionProblem) =>(Add(ep, Sub(Value(10), Variable("a"))), Add(ep, Sub(Value(10), Variable("a"))).eval))
    val transition2:State[ExpressionProblem, Int] = State((ep:ExpressionProblem) =>(Add(Add(Value(10), Variable("b")),ep), Sub(ep, Add(Value(10), Variable("a"))).eval))
    val res:State[ExpressionProblem, Int] = transition1.flatMap(v1=>transition2.map(v2=>v2))
    println(res.run(Add(Add(Variable("a"), Value(10)), Sub(Value(2), Variable("b")))).value)
    val forfor = for {
      _ <- State((ep:ExpressionProblem) =>(Add(ep, Sub(Value(10), Variable("a"))), Add(ep, Sub(Value(10), Variable("a"))).eval))
      t2 <- State((ep:ExpressionProblem) =>(Add(Add(Value(10), Variable("b")),ep), Sub(ep, Add(Value(10), Variable("a"))).eval))
    } yield t2
    println(forfor.run(Add(Add(Variable("a"), Value(10)), Sub(Value(2), Variable("b")))).value)

