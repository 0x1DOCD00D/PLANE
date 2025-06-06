/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package MultiProcessor;

public class ThreadStopNow {
    static volatile long progress = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread victim = new Thread(() -> {
            try {
                while (true) {
                    if( progress++ % 1000000 == 0) {
                        System.out.println("Victim is running, progress = " + progress);
                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Victim interrupted, exiting loop");
                            break;  // exit the loop if interrupted
                        }
                    }
                }
            } finally {
                System.out.println("victim finally executed");
            }
        }, "victim");
        victim.start();

        Thread.sleep(100);          // let the victim loop a bit

        System.out.println("Main thread calling victim.interrupt() …");
        victim.interrupt();    

        victim.join();
        System.out.println("Victim stopped, progress = " + progress);
    }
}
