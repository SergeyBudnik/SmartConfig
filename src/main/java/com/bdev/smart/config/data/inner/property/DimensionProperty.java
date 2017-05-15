package com.bdev.smart.config.data.inner.property;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class DimensionProperty extends ConditionalProperty {
    @Getter @Setter private Map<String, String> dimensions = new HashMap<>();

    public DimensionProperty(String name, Object value, PropertyType type) {
        super(name, value, type);
    }

    public void addDimension(String name, String value) {
        if (dimensions.containsKey(name)) {
            throw new RuntimeException();
        }

        dimensions.put(name, value);
    }
}
