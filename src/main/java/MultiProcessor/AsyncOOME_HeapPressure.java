/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package MultiProcessor;

// AsyncOOME_HeapPressure.java    (compile →  javac AsyncOOME_HeapPressure.java)
// Run with:  java -Xmx32m -XX:+DisableAttachMechanism -Dcom.sun.management.jmxremote=false AsyncOOME_HeapPressure
import java.util.ArrayList;

public class AsyncOOME_HeapPressure {
    public static void main(String[] args) {
        // ①  Daemon thread that *never lets go* of the heap.
        Thread hog = new Thread(() -> {
            var dump = new ArrayList<byte[]>();
            for (;;) {
                try {
                    dump.add(new byte[1_048_576]);        // 1 MiB each
                } catch (OutOfMemoryError swallowed) {
                    System.out.println("swallowed OOME in hog, size = " + dump.size());
                    try { Thread.sleep(10); } catch (InterruptedException ignore) {}
                }
            }
        }, "hog");
        hog.setDaemon(true);
        hog.start();

        long checksum = 0;
        try {
            while (true) {
                checksum ^= System.nanoTime();
            }
        } catch (OutOfMemoryError async) {
            System.out.println("async OOME in main, checksum = " + checksum);
            async.printStackTrace();
        }
    }
}
