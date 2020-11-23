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

import Variance.HierarchyOfScholars.*;

import java.util.ArrayList;

//PECS is a mnemonic device created by Josh Bloch to help remember Producer extends, Consumer super.

public class PECS {
    public void consumeItemsIntoScholarList(ArrayList<? super Student> studentList) {
//        we can insert into the arraylist objects of some type that is a subtype of Student
        studentList.add(new Student());
        studentList.add(new Junior());
        studentList.add(new PhD());
        studentList.add(new Undergraduate());
//        studentList.add(new Professor());
//        studentList.add(new Scholar());
//        We cannot obtain an object
//        Student genericStudent = studentList.get(0);
        Object studentObject = studentList.get(0);
        System.out.println(studentObject.getClass().toString());
    }

    public Student produceItemsFromScholarList(ArrayList<? extends Student> studentList, int index) {
//        studentList.add(new Student());
        if (index >= 0 && index < studentList.size())
            return studentList.get(index);
        return null;
    }

    public ArrayList<? super Student> getStudentList() {
        ArrayList<Scholar> scholars = new ArrayList<>();
        scholars.add(new Professor());
        scholars.add(new Junior());
        return scholars;
    }


    public static void main(String[] args) {
//        Liskov's substitutivity works
        Professor prof = new Untenured();
        ArrayList<Untenured> untenuredProfessors = new ArrayList<Untenured>();
        untenuredProfessors.add(new Research());
        untenuredProfessors.add(new TeachingTrack());
        untenuredProfessors.add(new Untenured());
//        java: incompatible types: java.util.ArrayList<Variance.HierarchyOfScholars.Untenured> cannot be converted to java.util.ArrayList<Variance.HierarchyOfScholars.Professor>
//        ArrayList<Professor> untenuredProfessors = new ArrayList<Untenured>();
        ArrayList<? extends Professor> professors = untenuredProfessors;
/*
        professors.add(new Untenured());
        java: incompatible types: Variance.HierarchyOfScholars.Untenured cannot be converted to capture#1 of ? extends Variance.HierarchyOfScholars.Professor
        it means that we cannot add new objects to the arraylist because we do not know what type of objects
        this arraylist holds except that this objects are of some subtype of the type Professor.
        However, we can obtain the content of the arraylist, since the contained objects have subtypes
        of the type Professor and they can be used in place of the variable whose type is Professor.
*/
        System.out.println("The list of untenured professors:");
        for (Professor utp : professors) {
//            no problem obtaining and accessing the objects in the arraylist
            System.out.println(utp.getClass().toString());
        }

        PECS pecs = new PECS();
        ArrayList<Scholar> scholars = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        pecs.consumeItemsIntoScholarList(scholars);
        pecs.consumeItemsIntoScholarList(students);
/*
        ArrayList<Tenured> tenuredProfs  = new ArrayList<>();
        pecs.consumeItemsIntoScholarList(tenuredProfs);
*/
        System.out.println("The list of scholars:");
        scholars.stream().forEach(s -> System.out.println(s.getClass().toString()));
        System.out.println("The list of students:");
        students.stream().forEach(s -> System.out.println(s.getClass().toString()));

        ArrayList<? super Student> whatIsThisList = pecs.getStudentList();
        whatIsThisList.add(new Graduate());
        System.out.println("The list of whom:");
        for (Object obj : whatIsThisList) {
//            no problem obtaining and accessing the objects in the arraylist
            System.out.println(obj.getClass().toString());
        }
//        Exception in thread "main" java.lang.ClassCastException: class Variance.HierarchyOfScholars.Professor cannot be cast to class Variance.HierarchyOfScholars.Student (Variance.HierarchyOfScholars.Professor and Variance.HierarchyOfScholars.Student are in unnamed module of loader 'app')
        Student student = pecs.produceItemsFromScholarList((ArrayList<? extends Student>) whatIsThisList, 0);
        System.out.println(student.getClass().toString());
    }
}
