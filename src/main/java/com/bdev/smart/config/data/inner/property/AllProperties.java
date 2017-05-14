package com.bdev.smart.config.data.inner.property;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AllProperties {
    private Map<String, Property> allProperties = new HashMap<>();

    public Property findOrCreateProperty(String name) {
        if (!allProperties.containsKey(name)) {
            allProperties.put(name, new Property());
        }

        return allProperties.get(name);
    }
}
