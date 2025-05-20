/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

//  ──────────────────────────────────────────────────────────────
//  ShadowScopes.java  –  many nested scopes, type erasure & 2× abnormal exit
//  ──────────────────────────────────────────────────────────────
import java.sql.SQLException;
import java.util.function.Supplier;

public class ShadowScopes<E extends Exception>   // E is a *checked* type parameter
{
    /* F0 –  STATIC-FIELD  whose declared type is superclass, run-time holds subtype */
    static ShadowScopes<? extends Exception> lab = new Nested<>();

    /* type name that *shadows* the type parameter ‘E’ in this scope               */
    static class E extends Throwable { }                       // shadowing a *type identifier*

    int E = 1;                               // variable shadows the inner class ‘E’

    /* ── nested static subtype that also shadows the static field name 'lab' ── */
    static class Nested<RX extends RuntimeException>
            extends ShadowScopes<RX>             // <RX> is still a *checked* variable!
            implements AutoCloseable {

        static String lab = "shadow";       // field shadows outer static ‘lab’

        static {                            // executed while outer ‘lab’ still null
            System.out.println("Nested.<clinit>: outer lab == " + ShadowScopes.lab);
        }

       // @Override                 // throws the *type variable* RX (erasure = Exception)
        void picky(RX t) throws RX {
            /* unchecked cast tricks the compiler: throws *checked* SQLException
               even though method is instantiated with RX = RuntimeException        */
            throw (RX)(Exception)new SQLException("sneaky");
        }

        @Override public void close() throws Exception {
            System.out.println("close explode");
            throw new Exception("close explode");
        }
    }

    /* method with generic bound; will throw twice because of a 'finally' that re-throws */
    <T extends Exception> void twoPhase(Supplier<T> sup) throws T {
        try {
            throw sup.get();                     // primary throwable
        } finally {
            throw (T)new RuntimeException("finally boom"); // secondary
        }
    }

    /* non-static helper with *name & variable* shadowing plus static-scope access */
    static void demo() throws Exception {

        /* local variable ‘lab’ shadows BOTH static fields named ‘lab’             */
        String lab = "local";
        try (Nested<RuntimeException> res = new Nested<>()) {  // open resource
            System.out.println("open resource");
            System.out.println("outer lab via qualifier = " + ShadowScopes.lab);   // outer field
            System.out.println("shadowed lab = " + lab);                          // local var

            // unchecked – raw view destroys type parameter information
            ShadowScopes raw = res;
            try {
                raw.picky(null);              // run-time throws SQLException (checked)
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

        } catch (RuntimeException ex) {    // catches finally-boomer
            System.out.println("finally throws: " + ex);
            for (Throwable s : ex.getSuppressed())
                System.out.println("    suppressed: " + s);
        } catch (Exception ex) {         // catches primary throwable
            System.out.println("primary throws: " + ex);
            for (Throwable s : ex.getSuppressed())
                System.out.println("    suppressed: " + s);
        } finally {
            System.out.println("finally block");
        }
        System.out.println("--- after demo ---");
    }

    /* method whose throws-type is the class parameter E (erasure = Exception) */
    void picky(E e) throws E { }           // overridden above

    // ───────────────   driver   ───────────────
    public static void main(String[] args) {
        try {
            demo();
            // explicit call that triggers the overridden picky()
            ((Nested<?>) lab).picky(null);
        } catch (Exception ex) {               // must catch: compile-time sees Exception
            System.out.println("caught exec: " + ex);
        }
    }
}
