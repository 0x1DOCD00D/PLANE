/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PuttingGenericConsumerInterface2Use<T> {
    List<T> list;
    Consumer<T> process;

    public PuttingGenericConsumerInterface2Use(List<T> list, Consumer<T> process) {
        this.list = list;
        this.process = process;
    }

    public void Process() {
        list.forEach(process);
    }

    public static void main(String[] args) {
        List<DecartesCoordinates> coordinates = new ArrayList<>();
        coordinates.add(new DecartesCoordinates(1, 2));
        coordinates.add(new DecartesCoordinates(2, 10));
        new PuttingGenericConsumerInterface2Use<>(coordinates, new Consumer<>() {
            public void accept(DecartesCoordinates decartesCoordinates) {
                System.out.println(decartesCoordinates.distance());
            }
        }).Process();
    }
}

