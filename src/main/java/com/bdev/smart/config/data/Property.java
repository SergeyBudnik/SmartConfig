package com.bdev.smart.config.data;


import com.bdev.smart.config.data.inner.PropertyType;

public class Property<T> {
    private T value;
    private PropertyType type;

    public T get() {
        return value;
    }

    PropertyType getType() {
        return type;
    }

    public void override(T value) {
        this.value = value;
    }
}
