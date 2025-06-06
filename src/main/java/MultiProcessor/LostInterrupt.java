/*
 Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package MultiProcessor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.Thread.sleep;

public class LostInterrupt {

    public static void main(String[] args) throws Exception {
        BlockingQueue<String> q = new ArrayBlockingQueue<>(1);

        /* Worker takes items from the queue for as long as it runs.      *
         * A casual “diagnostic” call to Thread.interrupted() is made at  *
         * the top of the loop – that single line clears the flag!        */
        Thread worker = new Thread(() -> {
            while (true) {
                // <<< the culprit: this *tests-and-clears* the flag >>>
/*
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
*/
                if (Thread.interrupted()) {                    
                    System.out.println("interrupt flag was set (now cleared)");
                }

                try {
                    System.out.println("worker waiting for item …");
                    String item = q.take();                     // should unblock on interrupt
                    System.out.println("worker got: " + item);
                } catch (InterruptedException ex) {             // never reached
                    System.out.println("graceful shutdown");
                    return;
                }
            }
        }, "worker");


        System.out.println("main interrupts worker");
        worker.interrupt();     // <-- signal should stop the thread
        worker.start();

        // give the interrupt time to propagate, then offer a value
        sleep(300);
        System.out.println("main puts item");
        q.put("hello");         // worker unblocks, keeps running

        sleep(500);
        System.out.println("worker alive? " + worker.isAlive());
    }
}
