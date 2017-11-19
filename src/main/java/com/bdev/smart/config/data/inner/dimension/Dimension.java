package com.bdev.smart.config.data.inner.dimension;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;

@EqualsAndHashCode(of = "name")
public class Dimension {
    @Getter
    private final String name;
    @Getter
    private final Set<DimensionValue> values = new HashSet<>();

    public Dimension(String name) {
        this.name = name;
    }

    public Optional<DimensionValue> getValue(String name) {
        return values.stream().filter(it -> it.getName().equals(name)).findAny();
    }

    public void addValue(DimensionValue value) {
        if (values.contains(value)) {
            throw new RuntimeException(format("Dimension '%s' already contains value '%s'", name, value.getName()));
        }

        values.add(value);
    }
}