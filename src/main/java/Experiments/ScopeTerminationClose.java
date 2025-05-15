/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

// ScopeTerminationDemo.java
public class ScopeTerminationClose {

    /* ── 1.  Static field initialiser (still leaks a partially-built object) ─ */
    static SpecialWorker defaultWorker = new SpecialWorker();

    /* ── 2.  Base class that is AutoCloseable ─ */
    static class Worker implements AutoCloseable {
        final String id = "Worker";
        Worker() {
            System.out.println(id + " ctor:   defaultWorker == " + defaultWorker);
        }
        void work() { System.out.println(id + " work()"); }
        @Override public void close() throws Exception {
            System.out.println(id + " close()");
        }
        public String toString() { return id; }
    }

    /* ── 3.  Subclass overriding work() and close() ─ */
    static class SpecialWorker extends Worker {
        SpecialWorker() { super(); }          // no longer throws
        @Override void work() { System.out.println("SpecialWorker work()"); }
        @Override public void close() throws Exception {
            System.out.println("SpecialWorker close() entered");
            super.close();
            throw new Exception("SpecialWorker close failure");
        }
    }

    /* ── 4.  Body that triggers normal & abrupt termination interplay ─ */
    public static void main(String[] args) {
        System.out.println("main(): defaultWorker = " + defaultWorker);

        try (Worker w = new SpecialWorker()) {        // resource guaranteed to close
            {   /* inner scope shadows the *field* name (§6.3) */
                SpecialWorker defaultWorker = (SpecialWorker)(Worker) null;  // legal cast-chain on null
                defaultWorker.work();                 // dynamic dispatch → NPE
            }
            System.out.println("unreachable");
        } catch (Throwable t) {                       // primary exception is the NPE
            System.out.println("top-level handler: " + t);
            for (Throwable s : t.getSuppressed())     // suppressed comes from close()
                System.out.println("  suppressed: " + s);
        }
    }
}
