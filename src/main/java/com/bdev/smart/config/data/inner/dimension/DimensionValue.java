package com.bdev.smart.config.data.inner.dimension;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(of = "name")
public class DimensionValue {
    @Getter private final String name;

    public DimensionValue(String name) {
        this.name = name;
    }
}
