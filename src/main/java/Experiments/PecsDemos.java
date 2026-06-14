/*
 * Copyright (c) 2026 Dr. Mark Grechanik and Lone Star Consulting, Inc.
 *
 * Created or updated on: 2026-06-14 12:28
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * All rights reserved. This software, source code, documentation, designs, algorithms, analyses, and related materials are the exclusive property of Dr. Mark Grechanik and Lone Star Consulting, Inc. No rights are granted to copy, modify, distribute, sublicense, publish, disclose, reverse engineer, or create derivative works from this material except by prior written authorization from Dr. Mark Grechanik or Lone Star Consulting, Inc.
 */

package Experiments;

import java.util.*;
import java.util.function.*;

/**
 * PECS — "Producer Extends, Consumer Super"
 * <p>
 * ? extends T   →  a PRODUCER. It hands T's OUT to you. You may READ T's; you may NOT add.
 * ? super   T   →  a CONSUMER. It takes T's IN from you. You may WRITE T's; reads come out as Object.
 * <p>
 * Mnemonic: if a parameter only PRODUCES values for you, write `? extends`.
 * if it only CONSUMES values from you, write `? super`.
 * if it does BOTH, use no wildcard (see demo 12).
 * <p>
 * Run all twelve angles:  java PecsDemos.java
 */
public class PecsDemos {

    public static void main(String[] args) {
        demo01_invariance();
        demo02_producerExtends();
        demo03_consumerSuper();
        demo04_copyBoth();
        demo05_producerIsReadOnly();
        demo06_consumerReadsAsObject();
        demo07_comparatorSuper();
        demo08_comparableSuper();
        demo09_stack();
        demo10_function();
        demo11_consumerSupplier();
        demo12_neither();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 1. INVARIANCE — the problem PECS exists to solve.
    //    Even though Integer IS-A Number, List<Integer> is NOT a List<Number>.
    // ─────────────────────────────────────────────────────────────────────────
    static void demo01_invariance() {
        System.out.println("== 1. Invariance: why we need wildcards at all ==");
        List<Integer> ints = List.of(1, 2, 3);
        // List<Number> nums = ints;   // does NOT compile — generics are invariant.
        // If it did, you could call nums.add(3.14) and smuggle a Double into a List<Integer>.
        // Wildcards (? extends / ? super) are the safe, opt-in way to relax this.
        System.out.println("  List<Integer> is not a List<Number>: " + ints + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 2. PRODUCER EXTENDS — read values that flow OUT of the collection.
    // ─────────────────────────────────────────────────────────────────────────
    // `producer` only gives us Numbers to read, so `? extends Number`.
    static double sumOf(Collection<? extends Number> producer) {
        double total = 0;
        for (Number n : producer) total += n.doubleValue();
        return total;
    }

    static void demo02_producerExtends() {
        System.out.println("== 2. Producer Extends: read from any subtype ==");
        List<Integer> ints = List.of(1, 2, 3);
        List<Double> dbls = List.of(1.5, 2.5);
        System.out.println("  sum of ints = " + sumOf(ints));
        System.out.println("  sum of dbls = " + sumOf(dbls) + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 3. CONSUMER SUPER — write values that flow INTO the collection.
    // ─────────────────────────────────────────────────────────────────────────
    // `sink` receives Integers from us, so `? super Integer`.
    static void fillWithZeros(List<? super Integer> sink, int count) {

        for (int i = 0; i < count; i++) sink.add(0);
    }

    static void demo03_consumerSuper() {
        System.out.println("== 3. Consumer Super: write into any supertype ==");
        List<Integer> ints = new ArrayList<>();
        List<Number> nums = new ArrayList<>();
        List<Object> objs = new ArrayList<>();
        fillWithZeros(ints, 2);
        fillWithZeros(nums, 3);   // Integer fits into a List<Number>
        fillWithZeros(objs, 5);   // ...and into a List<Object>
        System.out.println("  " + ints + " " + nums + " " + objs + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 4. BOTH IN ONE SIGNATURE — the canonical copy(src, dst).
    //    src produces T's (extends); dst consumes T's (super).
    // ─────────────────────────────────────────────────────────────────────────
    static <T> void copy(List<? extends T> src, List<? super T> dst) {

        dst.addAll(src);
    }

    static void demo04_copyBoth() {
        System.out.println("== 4. PECS together: copy(producer, consumer) ==");
        List<Integer> src = List.of(10, 20, 30);   // produces Integers
        List<Number> dst = new ArrayList<>();      // consumes Integers (Number is a supertype)
        copy(src, dst);
        System.out.println("  copied Integers into a List<Number>: " + dst + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 5. WHY EXTENDS IS READ-ONLY.
    // ─────────────────────────────────────────────────────────────────────────
    static void demo05_producerIsReadOnly() {
        System.out.println("== 5. A ? extends producer is read-only ==");
        List<? extends Number> producer = List.of(1, 2, 3);
        // producer.add(4);     // won't compile
        // producer.add(4.0);   // won't compile
        // The compiler can't prove the real element type (Integer? Double?),
        // so it refuses EVERY add (except null). But reading is always safe:
        Number first = producer.getFirst();     // a Number is guaranteed
        System.out.println("  can read as Number (" + first + "), cannot add\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 6. WHY SUPER READS COME OUT AS Object.
    // ─────────────────────────────────────────────────────────────────────────
    static void demo06_consumerReadsAsObject() {
        System.out.println("== 6. A ? super consumer reads back only as Object ==");
        List<? super Integer> consumer = new ArrayList<Number>();
        consumer.add(42);                   // writing an Integer is safe
        // Integer x = consumer.get(0);     // element could be Number or Object
        Object x = consumer.getFirst();         // Object is the only guarantee
        System.out.println("  wrote Integer, read back as Object: " + x + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 7. Comparator<? super T> — reuse a more general comparator.
    //    (This is exactly the signature of Collections.max / Collections.sort.)
    // ─────────────────────────────────────────────────────────────────────────
    static <T> T max(Collection<? extends T> coll, Comparator<? super T> cmp) {
        Iterator<? extends T> it = coll.iterator();
        T best = it.next();
        while (it.hasNext()) {
            T next = it.next();
            if (cmp.compare(next, best) > 0) best = next;
        }
        return best;
    }

    static void demo07_comparatorSuper() {
        System.out.println("== 7. Comparator<? super T>: a comparator can be more general ==");
        // Written to compare ANY Object (by text length)...
        Comparator<Object> byTextLength = Comparator.comparingInt(o -> o.toString().length());
        List<Integer> nums = List.of(5, 100, 42, 9999);
        // ...yet it happily compares Integers, because Integer <: Object (super).
        System.out.println("  longest-when-printed: " + max(nums, byTextLength) + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 8. Comparable<? super T> — inherit a natural ordering from a supertype.
    //    The famous bound `<T extends Comparable<? super T>>`.
    // ─────────────────────────────────────────────────────────────────────────
    static class Animal implements Comparable<Animal> {
        final String name;
        final int weight;

        Animal(String name, int weight) {
            this.name = name;
            this.weight = weight;
        }

        public int compareTo(Animal o) {
            return Integer.compare(weight, o.weight);
        }

        public String toString() {
            return name + "(" + weight + "kg)";
        }
    }

    static class Dog extends Animal {          // Dog has NO compareTo of its own
        Dog(String name, int weight) {
            super(name, weight);
        }
    }

    static <T extends Comparable<? super T>> T maxNatural(List<? extends T> list) {
        T best = list.getFirst();
        for (T t : list) if (t.compareTo(best) > 0) best = t;
        return best;
    }

    static void demo08_comparableSuper() {
        System.out.println("== 8. Comparable<? super T>: inherited ordering still works ==");
        List<Dog> dogs = List.of(new Dog("Bo", 12), new Dog("Rex", 30), new Dog("Max", 25));
        // Dog's ordering lives in Comparable<Animal>, i.e. Comparable<? super Dog>.
        System.out.println("  heaviest dog: " + maxNatural(dogs) + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 9. Stack.pushAll / popAll — the original Effective Java motivation.
    // ─────────────────────────────────────────────────────────────────────────
    static class Stack<E> {
        private final Deque<E> items = new ArrayDeque<>();

        void push(E e) {
            items.push(e);
        }

        E pop() {
            return items.pop();
        }

        boolean isEmpty() {
            return items.isEmpty();
        }

        void pushAll(Iterable<? extends E> src) {       // src PRODUCES E's → extends
            for (E e : src) push(e);
        }

        void popAll(Collection<? super E> dst) {        // dst CONSUMES E's → super
            while (!isEmpty()) dst.add(pop());
        }
    }

    static void demo09_stack() {
        System.out.println("== 9. Stack: pushAll(? extends) + popAll(? super) ==");
        Stack<Number> stack = new Stack<>();
        stack.pushAll(List.of(1, 2, 3));        // push Integers into a Number stack
        List<Object> dump = new ArrayList<>();
        stack.popAll(dump);                      // pop Numbers out into an Object list
        System.out.println("  popped into List<Object>: " + dump + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 10. Function<? super T, ? extends R> — input is consumed, output is produced.
    //     (This is the shape used by Stream.map and Function.andThen.)
    // ─────────────────────────────────────────────────────────────────────────
    static <T, R> List<R> mapAll(List<? extends T> in, Function<? super T, ? extends R> fn) {
        List<R> out = new ArrayList<>();
        for (T t : in) out.add(fn.apply(t));
        return out;
    }

    static void demo10_function() {
        System.out.println("== 10. Function<? super T, ? extends R> ==");
        List<Integer> ints = List.of(1, 2, 3);
        // fn takes a Number (super of Integer) and returns an Integer (a Number, so extends R=Number).
        Function<Number, Integer> doubler = n -> n.intValue() * 2;
        List<Number> doubled = mapAll(ints, doubler);
        System.out.println("  doubled: " + doubled + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 11. Supplier<? extends T> produces; Consumer<? super T> consumes.
    // ─────────────────────────────────────────────────────────────────────────
    static <T> void feed(Supplier<? extends T> source, Consumer<? super T> sink, int n) {
        for (int i = 0; i < n; i++) sink.accept(source.get());
    }

    static void demo11_consumerSupplier() {
        System.out.println("== 11. Supplier<? extends T> + Consumer<? super T> ==");
        Supplier<Integer> source = () -> 7;                       // produces Integers
        Consumer<Object> sink = o -> System.out.println("  got: " + o); // consumes Objects
        feed(source, sink, 3);                                    // T inferred as Integer
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 12. THE LIMIT OF PECS — when a parameter is BOTH produced and consumed,
    //     no wildcard works; the type must stay invariant.
    // ─────────────────────────────────────────────────────────────────────────
    static <T> void rotateInPlace(List<T> list) {   // not ? extends, not ? super — exact T
        if (list.isEmpty()) return;
        T first = list.removeFirst();   // READS a T out  (a ? super list couldn't yield a T)
        list.add(first);            // WRITES a T back (a ? extends list couldn't accept one)
    }

    static void demo12_neither() {
        System.out.println("== 12. No wildcard when you both read AND write ==");
        List<String> words = new ArrayList<>(List.of("a", "b", "c"));
        rotateInPlace(words);
        System.out.println("  rotated: " + words + "\n");
    }
}