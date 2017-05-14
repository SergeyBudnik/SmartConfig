package com.bdev.smart.config.data.inner;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.property.AllProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConfigInfo {
    private AllDimensions dimensions;
    private AllProperties propertiesInfo;
}
