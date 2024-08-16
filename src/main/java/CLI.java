/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

import java.util.Scanner;

public class CLI {
    public boolean getCLI(int allowed) {
        System.out.print("Enter an integer, preferrably " + allowed + ": ");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt() == allowed;
    }

    private int getCLINumber(Scanner sc) {
        try {
            if(sc != null) {
                sc.nextInt();
            } else {
                System.out.println("Scanner is null");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Invalid input, try again.");
        }
        return getCLINumber(sc);
    }
    public int getCLI() {
        System.out.print("Enter an integer: ");
        Scanner sc = new Scanner(System.in);
        return getCLINumber(sc);
    }
    public static void main(String[] args) {
        System.out.println(new CLI().getCLI(1));
    }
}
