/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

public class TypeInferenceGenerics {
    class G   { static void g(Object x) {} }
    class H<T> {
        static <T> void h(T x, T y) {}
        static void m() { h("", 1); }  // mixes String and Integer
    }
    record R(String s) {}

    public static void main(String[] args) {
        G g = new TypeInferenceGenerics().new G();
        TypeInferenceGenerics.G.g(null);
        g.g(null); // null is a legal argument for Object
        g.g("hello"); // String is a subtype of Object
        g.g(1); // Integer is a subtype of Object

        H<String> h = new TypeInferenceGenerics().new H<>();
        h.h("hello", "world"); // both arguments are String
        h.h("hello", 1); // mixes String and Integer
        h.h(new R("howdy"), 1); // mixes String and Integer
    }
}
