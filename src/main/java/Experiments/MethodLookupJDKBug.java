/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

//https://bugs.openjdk.org/browse/JDK-7087658

public class MethodLookupJDKBug {
    interface A {
        Iterable m(List<String> ls);
    }

    interface B {
        Iterable<String> m(List l);
    }

    interface AB extends A, B { }

    interface AA extends A { }

    public static void main(String[] args) {
        try {
            MethodHandle mh1 = MethodHandles.lookup().findVirtual(A.class, "m", MethodType.methodType(Iterable.class, List.class));
            MethodHandle mh2 = MethodHandles.lookup().findVirtual(B.class, "m", MethodType.methodType(Iterable.class, List.class));
            MethodHandle mh3 = MethodHandles.lookup().findVirtual(AB.class, "m", MethodType.methodType(Iterable.class, List.class));
            MethodHandle mh4 = MethodHandles.lookup().findVirtual(AA.class, "m", MethodType.methodType(Iterable.class, List.class));
        } catch (NoSuchMethodException e) {
            System.out.println("NoSuchMethodException for A.m(List<String>)");
        } catch (IllegalAccessException e) {
            System.out.println("Illegal access exception: " + e.getMessage());
        }
        catch (Throwable e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }
        
    }
}
