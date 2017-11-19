package com.bdev.smart.config.data.inner.property;

import com.bdev.smart.config.data.inner.dimension.Point;
import lombok.Getter;

public class PointProperty extends ConditionalProperty {
    @Getter private Point point;

    public PointProperty(Property parent, String name, Object value, PropertyType type, Point point) {
        super(parent, name, value, type);

        this.point = point;
    }
}
