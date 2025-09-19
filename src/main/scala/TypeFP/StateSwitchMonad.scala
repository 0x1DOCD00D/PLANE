////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

//The state is defined by the attributes and their values. There is also a transition function that switches from one state to the next.
//This function SwitchPT takes a state and it makes a transition to produce a tuple that contains some computed value
//and the new state that is transitioned from the previous one.
case class AbstractState[ValueType, State](Switch: State => Tuple2[ValueType, State]) {
  //the method unit takes a value of some time and wraps it into the class AbstractState, i.e., the state is created
  //from the value. For the function SwitchPT we create a simple function that takes the value of some state and returns a tuple
  def unit[SomeOtherType](input: SomeOtherType): AbstractState[SomeOtherType, State] = AbstractState(state => (input, state))

  //the function bind converts the value of one type and the state into a value of some different type with a transitioned state.
  //We define a SwitchPT function that is passed as the parameter to the newly created state. This function takes the state value and
  //applies the function SwitchPT to it that we passed to the encompassing case class resulting in an instance of the tuple.
  def flatMap[SomeOtherType](typeConversionFunction: ValueType => AbstractState[SomeOtherType, State]): AbstractState[SomeOtherType, State] = {
    AbstractState(state => {
      val newTupleState = Switch(state)
      typeConversionFunction(newTupleState._1).Switch(newTupleState._2)
    })
  }

  //the function map has the well-defined signature
  def map[SomeOtherType](typeConversionFunction: ValueType => SomeOtherType): AbstractState[SomeOtherType, State] = {
    //we take a wrapped stored value, apply the function typeConversionFunction to it to convert to a different type and then unit it
    flatMap(storedValue => unit(typeConversionFunction(storedValue)))
  }
}

object StateSwitchMonad extends App {

  //let's create a simple state machine that counts from one to two
  //and then switches back to one. We have two states, S1 and S2 and the switch functions
  //that transition from S1->S2 and from S2->S1.

  trait Counter {
    val v: Int
  }

  trait Descriptor {
    val v: String
  }


  type COUNTER = Counter => (Int, Counter)
  val switchFunction: COUNTER = counter => {
    if (counter.v == 2) {
      (1, new Counter {
        override val v: Int = 1
      })
    } else {
      (2, new Counter {
        override val v: Int = 2
      })
    }
  }

  class SCounter(switch_1_2: COUNTER) extends AbstractState(switch_1_2)

  val init = new SCounter(switchFunction)
  val unitResult = init.unit("5")
  val converted = unitResult.map(s => s.toInt)
  converted.Switch(new Counter {
    override val v: Int = 2
  })
  val nextState_2 = init.Switch(new Counter {
    override val v: Int = 2
  })
  val nextState_1 = init.Switch(nextState_2._2)
  val nextState_2_1 = init.Switch(nextState_1._2)

}
