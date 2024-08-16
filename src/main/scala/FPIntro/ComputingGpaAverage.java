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

class GradePoint {
    private Integer grade;
    private Double weight;

    public GradePoint(Integer grade, Double weight) {
        this.grade = grade;
        this.weight = weight;
    }

    public Double grade() {
        return grade * weight;
    }
}

public class ComputingGpaAverage {
    public static void main(String[] args) {
        List<GradePoint> grades = new ArrayList<>();
        grades.add(new GradePoint(5, 1.));
        grades.add(new GradePoint(2, 0.2));
        Double sum = 0e0;
        for (GradePoint g : grades) {
            sum += g.grade();
        }
        System.out.println(sum / grades.size());
    }
}
