/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

//https://bugs.openjdk.org/browse/JDK-8354110

class Scratch {
    public static void main(String[] args) {
        var one = new TypeOne(1, null);
        var updated = withComments(one, "Hello, type inference.");
        System.out.println(updated);
    }

    public static <T extends SimpleSum> T withComments(T base, String comments) {
        return (T) switch (base) {
            case TypeOne typeOne -> new TypeOne(typeOne.id(), comments);
            case TypeTwo typeTwo -> new TypeTwo(typeTwo.id(), comments);
        };
    }

    sealed interface SimpleSum permits TypeOne, TypeTwo {}

    record TypeOne(long id, String comments) implements SimpleSum {}
    record TypeTwo(long id, String comments) implements SimpleSum {}
}