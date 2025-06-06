/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package MultiProcessor;

// AsyncOOME_NativeThreads.java
public class AsyncOOME_NativeThreads {
    public static void main(String[] args) {
        try {
            while (true) {
                // Each new Thread reserves a native stack (1-2 MiB on most OSes).
                new Thread(() -> {
                    try { Thread.sleep(Long.MAX_VALUE); } catch (InterruptedException ignored) {}
                }).start();                      // ← error will surface here
            }
        } catch (OutOfMemoryError oom) {         // main catches it
            System.out.println("main thread caught: " + oom);
        }
    }
}
