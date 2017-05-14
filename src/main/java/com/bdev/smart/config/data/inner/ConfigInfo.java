package com.bdev.smart.config.data.inner;

import com.bdev.smart.config.data.inner.dimension.DimensionInfo;
import com.bdev.smart.config.data.inner.property.PropertyInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class ConfigInfo {
    private Map<String, DimensionInfo> dimensions;
    private Map<String, PropertyInfo> propertiesInfo;
}
