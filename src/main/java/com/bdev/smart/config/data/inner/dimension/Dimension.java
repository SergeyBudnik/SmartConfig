package com.bdev.smart.config.data.inner.dimension;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Dimension {
    private AllDimensions allDimensions;

    private String name;
    private Set<String> values = new HashSet<>();

    Dimension(AllDimensions allDimensions, String name) {
        this.allDimensions = allDimensions;

        this.name = name;
    }

    public void addValue(String value) {
        if (values.contains(value)) {
            throw new RuntimeException();
        }

        values.add(value);

        allDimensions.onNewDimensionValueAdded();
    }

    public boolean containsValue(String value) {
        return values.contains(value);
    }
}
