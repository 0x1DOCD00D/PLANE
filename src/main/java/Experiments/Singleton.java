/*
 * Copyright (c) 2026 Dr. Mark Grechanik and Lone Star Consulting, Inc.
 *
 * Created or updated on: 2026-06-14 11:27
 *
 * All rights reserved. This software, source code, documentation, designs, algorithms, analyses, and related materials are the exclusive property of Dr. Mark Grechanik and Lone Star Consulting, Inc. No rights are granted to copy, modify, distribute, sublicense, publish, disclose, reverse engineer, or create derivative works from this material except by prior written authorization from Dr. Mark Grechanik or Lone Star Consulting, Inc.
 */

package Experiments;

public class Singleton {
    private static  Singleton instance = null;
    public static Singleton getInstance() { 
        if(instance == null) instance = new Singleton();
        return instance; }
     private Singleton() { }

    public static void main(String[] args) {
        var s1 = Singleton.getInstance();
        var s2 = Singleton.getInstance();
        System.out.println(s1 == s2);
    }
}
