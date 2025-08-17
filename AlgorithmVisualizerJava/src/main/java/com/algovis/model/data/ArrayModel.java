package com.algovis.model.data;

import java.util.Arrays;

public class ArrayModel {
    public final int[] a;

    public ArrayModel(int[] a) { this.a = Arrays.copyOf(a, a.length); }
    public int size() { return a.length; }
    public int get(int i) { return a[i]; }
    public void set(int i, int v) { a[i] = v; }
    public ArrayModel copy() { return new ArrayModel(a); }
}