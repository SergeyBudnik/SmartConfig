package com.bdev.smart.config.data.inner;

import lombok.Data;

import java.util.Collection;

@Data
public class PropertyInfo {
    private PropertyType type;
    private Collection<DimensionPropertyInfo> dimensionPropertyInfo;
}
