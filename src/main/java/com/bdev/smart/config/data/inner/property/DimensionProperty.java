package com.bdev.smart.config.data.inner.property;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class DimensionProperty {
    private Map<String, String> dimensions = new HashMap<>();
    private Object value;
    private PropertyType type;

    public void addDimension(String name, String value) {
        if (dimensions.containsKey(name)) {
            throw new RuntimeException();
        }

        dimensions.put(name, value);
    }
}
