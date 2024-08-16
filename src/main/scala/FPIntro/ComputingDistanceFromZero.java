/*
 * Copyright (c) 2022 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro;

import java.util.ArrayList;
import java.util.List;

class DecartesCoordinates {
    private final Integer x;
    private final Integer y;

    public DecartesCoordinates(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Double distance() {
        return Math.sqrt(x * x + y * y);
    }
}

public class ComputingDistanceFromZero {
    public static void main(String[] args) {
        List<DecartesCoordinates> coordinates = new ArrayList<>();
        coordinates.add(new DecartesCoordinates(1, 2));
        coordinates.add(new DecartesCoordinates(2, 10));
        for (DecartesCoordinates c : coordinates) {
            System.out.println(c.distance());
        }
    }
}
