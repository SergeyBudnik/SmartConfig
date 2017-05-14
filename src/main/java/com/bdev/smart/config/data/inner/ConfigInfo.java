package com.bdev.smart.config.data.inner;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.property.PropertyInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class ConfigInfo {
    private AllDimensions dimensions;
    private Map<String, PropertyInfo> propertiesInfo;
}
