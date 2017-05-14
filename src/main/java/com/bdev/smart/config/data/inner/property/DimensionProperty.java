package com.bdev.smart.config.data.inner.property;

import lombok.Data;

import java.util.Set;

@Data
public class DimensionProperty {
    private Set<String> dimensions;
    private Object value;
    private PropertyType type;
}
