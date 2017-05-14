package com.bdev.smart.config.data.inner.dimension;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Dimension {
    private AllDimensions allDimensions;
    private Set<String> values = new HashSet<>();

    Dimension(AllDimensions allDimensions) {
        this.allDimensions = allDimensions;
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