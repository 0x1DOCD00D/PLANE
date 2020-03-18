/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro;

//this is a straightforward implementation, like
interface AddIntegersFunction {
    default Integer add(Integer a, Integer b) {
        return a + b;
    }
}

//
interface AddIntegersAbstractFunction {
    Integer add(Integer a, Integer b);
}

interface AddStringsAbstractFunction {
    String add(String a, String b);
}

interface AddGenericFunction<T> {
    T add(T a, T b);
}

interface AddDiverseGenericFunction<A, B, C> {
    C add(A a, B b);
}

interface AddDiverseGenericEmptyFunction<A, B, C> {
}


public class ModelFunctions {
    public static void main(String[] args) {
        Integer result = new AddIntegersFunction() {
        }.add(3, 5);
        System.out.println(result);

        result = new AddIntegersAbstractFunction() {
            @Override
            public Integer add(Integer a, Integer b) {
                return a + b;
            }
        }.add(2, 11);
        System.out.println(result);

        result = new AddIntegersAbstractFunction() {
            @Override
            public Integer add(Integer a, Integer b) {
                assert (a >= 0 && b >= 0);
                if (b == 0) return a;
                return add(++a, --b);
            }
        }.add(2, 11);
        System.out.println(result);

        System.out.println(new AddStringsAbstractFunction() {
            @Override
            public String add(String a, String b) {
                return a.concat(b);
            }
        }.add("Mark G. teaches ", "CS474"));

        System.out.println(new AddGenericFunction<Double>() {
            @Override
            public Double add(Double a, Double b) {
                return a + b;
            }
        }.add(23.5, 7.5));

        System.out.println(new AddDiverseGenericFunction<Integer, Double, String>() {
            @Override
            public String add(Integer a, Double b) {
                return a.toString().concat(b.toString());
            }
        }.add(10, 1.1E10));

        System.out.println(new AddDiverseGenericEmptyFunction<Integer, Double, String>() {
            public String add(Integer a, Double b) {
                return a.toString().concat(b.toString());
            }
        }.add(10, 1.1E10));
    }
}
