/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

import java.util.*;

public class HeapPollution {

    // ➊  GENERIC + VARARGS  (§8.4.1, §9.6.4.7)
    @SafeVarargs                      // silences “generic array creation” / “heap-pollution” warnings
    private static <T> List<T> corrupt(List<T>... lists) {
        // lists has runtime type List[] but compile-time type List<T>[]
        Object[] asObjectArray = lists;          // array covariance gateway
        asObjectArray[0] = Arrays.asList("not-an-integer");
        // — at this point lists[0] refers to a List<String>,
        //   yet the compiler still believes it is a List<T>.
        return lists[0];                         // static return type List<T>
    }

    public static void main(String[] args) {

        // ➋  RAW / UNCHECKED CALL  (§4.8, §5.1.9)
        @SuppressWarnings("unchecked")
        List<Integer> ints = corrupt(new ArrayList<Integer>());

        // ➌  AUTOBOX / UNBOX  (§5.1.7)
        for (int x : ints) {                     // tries to unbox the first element
            System.out.println(x);
        }
    }
}
