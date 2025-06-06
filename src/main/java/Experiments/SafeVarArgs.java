/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

import java.util.List;

public class SafeVarArgs {
    @SafeVarargs
    static <T> void sink(List<T>... lists) {
        Object[] raw = lists;                 
        raw[0] = List.of(42);             
        T t = lists[0].get(0);                
        System.out.println(t);                
    }
    public static void main(String[] args) {
        try {
            sink(List.of("Hello"), List.of("World"));
        } catch (ClassCastException e) {
            System.out.println("Caught ClassCastException: " + e.getMessage());
        }
    }
}
