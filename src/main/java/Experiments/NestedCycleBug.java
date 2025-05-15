/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

public class NestedCycleBug {

    /* ─────── helpers from the previous example ─────── */
    interface Pinger { default void ping() { System.out.println("P-ping"); } }
    static class Special implements Pinger, AutoCloseable {
        @Override public void close() { System.out.println("Special.close"); }
    }

    /* F₀ₒ — subtype-reference tag that starts/ends the outer cycle */
    static Pinger holder = new Special();

    /* ─────── inner 3-node cycle (unchanged) ─────── */
    private static void invokeBug() {
        Pinger p = (Pinger)(Object)null;          // F₁ᵢ  null-cast to iface
        try {
            p.ping();                             // R₁ᵢ  default-method dispatch → NPE
        } catch (NullPointerException ex) {       // F₂ᵢ  abrupt exit
            System.out.println("inner NPE caught");
        }                                         // R₂ᵢ  control flow rejoins → cycle closes
    }

    /* ─────── overloaded family  (all compile, all receive null) ─────── */
    static void overload(String         s) { System.out.println("String overload"); }
    static void overload(Integer        i) { System.out.println("Integer overload"); }
    static void overload(Number         n) { System.out.println("Number  overload"); }
    static void overload(Special        s) { System.out.println("Special overload"); }
    static void overload(Object...      v) { System.out.println("var-args overload"); }

    interface X { }// X is a subtype of String
    public static void main(String[] args) {

        System.out.println("main sees holder = " + holder);      // R₀ₒ

        try (Special res = new Special()) {                      // F₁ₒ  resource subtype
            invokeBug();                                         // inner cycle

            /* R₁ₒ — overload resolution driven by *static* type of (cast) null */

            overload((Special)     null);   // exact match → Special overload
            overload((Integer)     null);   // exact match → Integer overload
            overload((X)null);   
            overload((Float)null);   
            overload((Object)null);                 // most specific is var-args

        } catch (Throwable t) {                                     // F₃ₒ
            System.out.println("outer catcher: " + t);
            for (Throwable s : t.getSuppressed())
                System.out.println("    suppressed: " + s);
        }                                                           // R₂ₒ: try-with-resources
    }
}
