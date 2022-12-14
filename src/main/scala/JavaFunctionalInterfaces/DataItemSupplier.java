/*
 * Copyright (c) 2022 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package JavaFunctionalInterfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class DataItemSupplier implements Supplier<List<DataItem>> {
    private final List<DataItem> listItems = new ArrayList<>();
    private final int howMany;
    private final int bound;

    public DataItemSupplier(int howMany, int bound) {
        this.howMany = howMany;
        this.bound = bound;
    }

    List<DataItem> produce(int left) {
        if (left <= 0) return listItems;
        else {
            listItems.add(new DataItem(new Random().nextInt() % bound));
        }
        return produce(howMany - 1);
    }

    @Override
    public List<DataItem> get() {
        return produce(howMany);
    }
}
