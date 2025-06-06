/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

public class SwitchEval {
    public static void main(String[] args) {
        eval(new Plus(new Value(1), new Value(2)));
    }
    private static int eval(Expr expr) {
        return switch (expr) {
            case Plus(Value v, var e) ->   // v is Value; e is Expr
                    throw new UnsupportedOperationException(v.toString());
            case Plus(var e, Value v) ->   // now e is Expr, v is Value again
                    throw new UnsupportedOperationException(v.toString());
            default -> 0;
        };
    }
    private sealed interface Expr permits Value, Plus {}
    private record Value(int value) implements Expr {}
    private record Plus(Expr left, Expr right) implements Expr {}
}
