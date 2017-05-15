package com.bdev.smart.config.data.inner.property;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class ConditionalProperty {
    private Object value;
    private PropertyType type;
}
