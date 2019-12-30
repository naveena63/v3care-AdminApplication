package com.ashesha.V3Care.Utils;

public class Singleton {
    private static final Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }

    public  int cartItemsCount = 0;
}