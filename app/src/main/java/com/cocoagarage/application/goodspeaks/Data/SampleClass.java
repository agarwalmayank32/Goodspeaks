package com.cocoagarage.application.goodspeaks.Data;

public class SampleClass {
    private static SampleClass ourInstance = new SampleClass();

    public static SampleClass getInstance() {
        return ourInstance;
    }

    private SampleClass() {
    }
}
