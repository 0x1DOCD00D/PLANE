/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Variance;

class SomeClass {
}

public class BasicVarianceRuntimeError {
    public static void main(String[] args) {
        String[] arrayOfStrings = new String[2];
        assert (arrayOfStrings != null);
        arrayOfStrings[0] = "DrMark";
        System.out.println(arrayOfStrings[0]);

/*
        Since an object of the type String is also an object of the type Object, we can use the
        Liskov's substitutivity principle to substitute the object of the type String in the location
        where the object of the type Object is needed. The Java compiler allows us to do that, however,
        the same is also true for the containers of the objects of some type. In this case the container is
        the array of String object and it is assigned to a variable that references an array of objects of the type Object.
        Essentially, both variables,arrayOfObjects and arrayOfStrings are aliases that reference the same
        location in the memory where the array object is stored. So the problem is not that an array of strings
        can be assigned to a variable that is declared an array of objects; a real problem is what happens
        when clients request objects from these arrays or other storages and when they store objects. What types
        of these objects can they expect? Clearly, if storing objects in the arrays was prohibited, we could easily
        disregard this issue, however, when one client can store arbitrary objects in this storage, what contract assurances
        about the types of these objects can be given to other clients?
*/

        Object[] arrayOfObjects = arrayOfStrings;
        System.out.println(arrayOfObjects[0]);
        /*
         * Exception in thread "main" java.lang.ArrayStoreException: java.lang.Double
         * at Variance.BasicVarianceRuntimeError.main(BasicVarianceRuntimeError.java:22)
         *
         * It mean that the runtime cannot store an object of the type Double representation
         * in the array of Strings. It would be nice to have this warning at compile time.
         * Without this runtime check it is possible to insert some object of some class and
         * this object will
         * */
        arrayOfObjects[0] = 3.14;
        arrayOfObjects[0] = new SomeClass();
    }
}
