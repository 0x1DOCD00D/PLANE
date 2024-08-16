/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package JavaFunctionalInterfaces;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;


class DataItem {
    String strVal;
    Integer intVal;

    public DataItem(int bound) {
        //the string size if bound by 100 bytes
        int strSize = new Random().nextInt(bound);
        byte[] array = new byte[strSize];
        new Random().nextBytes(array);
        //let's make the characters printable
        for (int i = 0; i < strSize; i++) {
            if (array[i] < 0) array[i] += 127;
            if (array[i] < 33) array[i] += 33;
        }
        strVal = new String(array, StandardCharsets.UTF_8);
        intVal = new Random().nextInt();
    }
}

public class ProduceListStreamOfItems {
    private List<DataItem> listItems = new ArrayList<>();

    List<DataItem> produce(int howMany, int bound) {
        if (howMany <= 0) return listItems;
        else {
            listItems.add(new DataItem(new Random().nextInt() % bound));
        }
        return produce(howMany - 1, bound);
    }

    public static void main(String[] args) {
        Function<Integer, Function<Integer, List<DataItem>>> listGenerator = bound -> listSize -> new ProduceListStreamOfItems().produce(listSize, bound);
        List<DataItem> lst = new ProduceListStreamOfItems().produce(10, 200);
        listGenerator.apply(10).apply(200).forEach(di -> System.out.println(di.intVal + ", " + di.strVal));
    }
}
