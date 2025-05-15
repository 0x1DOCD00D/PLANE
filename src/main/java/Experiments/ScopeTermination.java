/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Experiments;

// ScopeTerminationDemo.java
public class ScopeTermination {

    /* ── static field initialiser ─────────────── (§12.4) */
    static Worker defaultWorker = new SpecialWorker();   // (1)

    /* ── base class that is AutoCloseable ─────── */
    static class Worker implements AutoCloseable {
        final String id = "Worker";
        Worker() {
            System.out.println(id + " ctor:   defaultWorker == " + defaultWorker);
        }
        void work() { System.out.println(id + " working"); }
        @Override public void close() throws Exception {
            System.out.println(id + " close");
        }
        @Override public String toString() { return id; }
    }

    /* ── subclass overriding close() ───────────── */
    static class SpecialWorker extends Worker {
        { /* instance-initialiser */ }
        SpecialWorker() {
            super();                                  // (2)
            if (defaultWorker == null)                // still null the first time!
                throw new IllegalStateException("field not yet assigned");
        }
        @Override void work() { System.out.println("SpecialWorker work"); }
        @Override public void close() throws Exception {
            System.out.println("SpecialWorker.close() invoked");
            super.close();
            throw new Exception("SpecialWorker close failure");
        }
    }

    public static void main(String[] args) {
        System.out.println("main(): defaultWorker = " + defaultWorker);
        try {
            run();
        } catch (Throwable t) {                       // (6)
            System.out.println("Top-level: " + t);
            for (Throwable s : t.getSuppressed())
                System.out.println("  suppressed: " + s);
        }
    }

    static void run() throws Exception {
        /* ── try-with-resources ─────────────────── (§14.20.3) */
        try (Worker w = new SpecialWorker()) {        // (3)
            {
                /* inner block shadows a *field* name (§6.3) */
                SpecialWorker defaultWorker = (SpecialWorker)(Worker) null;  // (4)
                defaultWorker.work();                 // dynamic dispatch → NPE (5)
            }
            System.out.println("unreachable");
        }
    }
}
