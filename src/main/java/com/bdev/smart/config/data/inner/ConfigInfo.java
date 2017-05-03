package com.bdev.smart.config.data.inner;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class ConfigInfo {
    private Map<String, DimensionInfo> dimensions;
    private Map<String, PropertyInfo> propertiesInfo;
}
