/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

public class VarianceCastNull {

    /* A tiny mutable wrapper that will carry the pollution around */
    static final class Box<T> {
        private T v;
        Box(T v)       { this.v = v; }
        T    get()     { return v; }
        void set(T nv) { v = nv; }
    }

    /* ➊  GENERIC  +  VARARGS  +  CONTRAVARIANCE  (§8.4.1, §4.10.2)           */
    @SafeVarargs                         // silences “generic array creation” warnings
    static void pollute(Box<? super Integer>... sinks) {

        /* ➋  RAW-CAST  (§4.8, §5.1.9) — hop out of the type system */
        Box raw0 = (Box) sinks[0];       // unchecked, compiles with only a warning
        Box raw1 = (Box) sinks[1];

        /* ➌  HEAP-POLLUTION: mutate the **contents** of a Box<Integer>        */
        raw0.set("polluted");            // store a String where Integer expected

        /* ➍  NULL-CAST ACROBATICS                                          */
        raw1.set((Integer) (Object) null);   // triple cast of null is always legal
    }

    public static void main(String[] args) {

        /* Caller owns two perfectly typed boxes */
        Box<Integer> intBox1 = new Box<>( 42 );
        Box<Integer> intBox2 = new Box<>(  7 );
        pollute(intBox1, intBox2);            // only unchecked warnings emitted

        /* ➎  COVARIANT ‘producer’ view  (§4.5.1):  ? extends Number           */
        Box<? extends Number> goodView1 = intBox1;
        Box<? extends Number> goodView2 = intBox2;

        /* ➏  Unbox — the polluted String triggers ClassCastException          */
        try {
            int value = goodView1.get().intValue();  // runtime object is String
        } catch (ClassCastException ex) {
            System.out.println("Variance surprise 1: " + ex);
        }

        /* ➐  Unbox the null — gives NullPointerException                      */
        try {
            int value = goodView2.get().intValue();  // runtime object is null
        } catch (NullPointerException ex) {
            System.out.println("Variance surprise 2: " + ex);
        }
    }
}
