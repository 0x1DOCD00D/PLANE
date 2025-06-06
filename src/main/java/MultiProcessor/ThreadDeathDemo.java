/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package MultiProcessor;

// AsyncThreadDeathDemo.java
// Compile:  javac --add-exports java.base/sun.misc=ALL-UNNAMED AsyncThreadDeathDemo.java
// Run:      java  --add-opens java.base/sun.misc=ALL-UNNAMED  AsyncThreadDeathDemo
import sun.misc.Unsafe;
import java.lang.reflect.Field;

public class ThreadDeathDemo {

    // Helper: obtain the singleton Unsafe instance via reflection
    private static Unsafe untrustedUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cannot access sun.misc.Unsafe", e);
        }
    }

    static volatile long progress = 0;

    public static void main(String[] args) throws InterruptedException {
        // Victim thread updates some invariant in a tight loop.
        Thread victim = new Thread(() -> {
            try {
                while (true) {
                    if( progress++ > 10_000_000) {
                        Unsafe u = untrustedUnsafe();
                        System.out.println("Injector thread throwing ThreadDeath into victim…");
                        u.throwException(new ThreadDeath() {
                            // Optional: override to make stack traces clearer
                            public synchronized Throwable fillInStackTrace() { return this; }
                        });                    }
                }
            } finally {
                System.out.println("victim finally block executed");
            }
        }, "victim");
        victim.start();

        Thread.sleep(1000);

        
        victim.join();
        System.out.println("Victim stopped. progress = " + progress);
    }
}
