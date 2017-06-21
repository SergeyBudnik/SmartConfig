package com.bdev.smart.config.exceptions;

public class PropertyIsInvalid extends RuntimeException {
    public PropertyIsInvalid(String propertyName) {
        super("Property '" + propertyName + "' is invalid.");
    }
}
