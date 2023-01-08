/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package MultiProcessor;

public class SimpleThreadCount {
    volatile int count = 0;
    int maxCount = 100;
    public void runCount(int threads) {
        Thread [] arrayOfThreads = new Thread[threads];
        for(int i = 0; i < threads; i++) {
            arrayOfThreads[i] = new Thread(() -> {
                for(int j = 0; j < maxCount/threads; j++) {
                    count++;
                    System.out.println("[" + Thread.currentThread().getId() + "] - " +count);
                }
            });
            arrayOfThreads[i].start();
        }
        for(int i = 0; i < threads; i++) {
            try {
                arrayOfThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Final count: " + count);
    }

    public static void main(String[] args) {
        System.out.println("Number of processors: " + Runtime.getRuntime().availableProcessors());
        new SimpleThreadCount().runCount(20);
    }
}
