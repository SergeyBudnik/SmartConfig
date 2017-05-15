package com.bdev.smart.config.data.inner.dimension;

import lombok.Data;

import java.util.*;

@Data
public class AllDimensions {
    private Map<String, Dimension> dimensions = new HashMap<>();

    public Dimension addDimension(String dimensionName) {
        if (dimensions.containsKey(dimensionName)) {
            throw new RuntimeException();
        }

        Dimension dimension = new Dimension(this, dimensionName);

        dimensions.put(dimensionName, dimension);

        return dimension;
    }

    public Dimension findDimensionByValue(String dimensionValue) {
        for (Dimension dimension : dimensions.values()) {
            if (dimension.containsValue(dimensionValue)) {
                return dimension;
            }
        }

        throw new RuntimeException();
    }

    void onNewDimensionValueAdded() {
        Set<String> uniqueDimensionValues = new HashSet<>();

        dimensions.forEach((dimensionName, dimension) ->
                dimension.getValues().forEach(dimensionValue -> {
                    if (uniqueDimensionValues.contains(dimensionValue)) {
                        throw new RuntimeException();
                    }

                    uniqueDimensionValues.add(dimensionValue);
                })
        );
    }
}
