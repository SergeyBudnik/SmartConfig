package com.bdev.smart.config.data.inner.property;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DimensionProperty {
    private Map<String, String> dimensions = new HashMap<>();
    private Object value;
    private PropertyType type;

    public DimensionProperty(Object value, PropertyType type) {
        this.value = value;
        this.type = type;
    }

    public void addDimension(String name, String value) {
        if (dimensions.containsKey(name)) {
            throw new RuntimeException();
        }

        dimensions.put(name, value);
    }
}
