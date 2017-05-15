package com.bdev.smart.config.data.inner.property;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DefaultProperty {
    private Object value;
    private PropertyType type;
}
