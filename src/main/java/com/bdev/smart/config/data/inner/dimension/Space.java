package com.bdev.smart.config.data.inner.dimension;

import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;

public class Space {
    @Getter private final Set<Dimension> dimensions = new HashSet<>();

    public Optional<Dimension> getDimensionByName(String name) {
        return dimensions.stream().filter(it -> it.getName().equals(name)).findAny();
    }

    public Optional<Dimension> getDimensionByValue(DimensionValue value) {
        return dimensions.stream().filter(it -> it.getValues().contains(value)).findAny();
    }

    public void addDimension(Dimension dimension) {
        if (dimensions.contains(dimension)) {
            throw new RuntimeException(format("Dimension '%s' already exists", dimension.getName()));
        }

        for (DimensionValue dimensionValue : dimension.getValues()) {
            for (Dimension existingDimension : dimensions) {
                if (existingDimension.getValue(dimensionValue.getName()).isPresent()) {
                    throw new RuntimeException(format("Dimension '%s' already contains value '%s'",
                            existingDimension.getName(),
                            dimensionValue.getName())
                    );
                }
            }
        }

        dimensions.add(dimension);
    }
}
