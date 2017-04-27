package com.bdev.smart.config;

public class SmartConfigProperty<T> {
    private T value;
    private SmartConfigPropertyType type;

    public T get() {
        return value;
    }

    SmartConfigPropertyType getType() {
        return type;
    }

    public void override(T value) {
        this.value = value;
    }
}
