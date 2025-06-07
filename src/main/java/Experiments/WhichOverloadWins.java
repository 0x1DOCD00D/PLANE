/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class WhichOverloadWins {
    static void choose(Function<Integer,Integer> f) {        // (1)
        System.out.println("Function<String,Integer>");
    }
    static void choose(UnaryOperator<Integer> u) {          // (2)
        System.out.println("UnaryOperator<Integer>");
    }

    public static void main(String[] args) {
        choose(x -> x.hashCode());
        choose((Integer x) -> x + 1);
    }
}
