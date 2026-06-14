/*
 * Copyright (c) 2026 Dr. Mark Grechanik and Lone Star Consulting, Inc.
 *
 * Created or updated on: 2026-06-14 11:27
 *
 * All rights reserved. This software, source code, documentation, designs, algorithms, analyses, and related materials are the exclusive property of Dr. Mark Grechanik and Lone Star Consulting, Inc. No rights are granted to copy, modify, distribute, sublicense, publish, disclose, reverse engineer, or create derivative works from this material except by prior written authorization from Dr. Mark Grechanik or Lone Star Consulting, Inc.
 */

package Experiments;

public enum Singleton {
    INSTANCE;

    public void doWork() {
        System.out.println("Working through enum singleton");
    }

    public static void main(String[] args) {
        var s1 = Singleton.INSTANCE;
        var s2 = Singleton.INSTANCE;

        System.out.println(s1 == s2);
        s1.doWork();
    }
}