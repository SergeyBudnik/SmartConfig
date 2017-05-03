package com.bdev.smart.config.data.inner;

import lombok.Data;

import java.util.Set;

@Data
public class DimensionPropertyInfo {
    private Set<String> dimensions;
    private Object value;
    private PropertyType type;
}
