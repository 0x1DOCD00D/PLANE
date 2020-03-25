/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package JavaFunctionalInterfaces;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataItemTest {
    @Test
    void CreateListofItems() {
        List<DataItem> lst_1 = new ProduceListStreamOfItems().produce(1, 100);
        List<DataItem> lst_0 = new ProduceListStreamOfItems().produce(0, 200);
        List<DataItem> lst_minus1 = new ProduceListStreamOfItems().produce(-1, 300);
        List<DataItem> lst_10 = new ProduceListStreamOfItems().produce(10, 10);
        List<DataItem> lst_1000 = new ProduceListStreamOfItems().produce(1000, 100);
        assertEquals(1, lst_1.size());
        assertEquals(0, lst_0.size());
        assertEquals(0, lst_minus1.size());
        assertEquals(10, lst_10.size());
        assertEquals(1000, lst_1000.size());
    }

}