package com.bdev.smart.config.data.inner.property;

import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.dimension.DimensionValue;
import com.bdev.smart.config.data.inner.dimension.Point;
import com.bdev.smart.config.data.util.Tuple;
import com.bdev.smart.config.exceptions.PropertyIsInvalid;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Property {
    private String name;
    private PropertyType type;
    private DefaultProperty defaultProperty;
    private Collection<PointProperty> dimensionsProperty = new ArrayList<>();

    Property(String name) {
        this.name = name;
    }

    public void setDefaultProperty(DefaultProperty defaultProperty) {
        if (this.defaultProperty != null) {
            throw new PropertyIsInvalid(name);
        }

        if (type == null) {
            type = defaultProperty.getType();
        } else {
            if (type != defaultProperty.getType()) {
                throw new PropertyIsInvalid(name);
            }
        }

        this.defaultProperty = defaultProperty;
    }

    public void addDimensionProperty(PointProperty dimensionProperty) {
        if (type == null) {
            type = dimensionProperty.getType();
        } else {
            if (type != dimensionProperty.getType()) {
                throw new PropertyIsInvalid(name);
            }
        }

        dimensionsProperty.add(dimensionProperty);
    }

    // ToDo: Test
    public ConditionalProperty getMostSuitableProperty(Point point) {
        List<Tuple<Integer, PointProperty>> weightedDimensionsPropertyInfo =
                new ArrayList<>();

        if (dimensionsProperty.size() == 0) {
            if (defaultProperty == null) {
                throw new PropertyIsInvalid(name);
            }

            return defaultProperty;
        } else {
            for (PointProperty dimensionProperty : dimensionsProperty) {
                weightedDimensionsPropertyInfo.add(
                        new Tuple<>(
                                calculatePropertyWeight(dimensionProperty, point),
                                dimensionProperty
                        )
                );
            }

            int maxWeight = weightedDimensionsPropertyInfo
                    .stream()
                    .mapToInt(Tuple::getA)
                    .max()
                    .orElseThrow(RuntimeException::new);

            List<PointProperty> dimensionPropertiesWithMaxWeight = weightedDimensionsPropertyInfo
                    .stream()
                    .filter(it -> it.getA() > 0)
                    .filter(it -> it.getA() == maxWeight)
                    .map(Tuple::getB)
                    .collect(Collectors.toList());

            if (dimensionPropertiesWithMaxWeight.size() <= 0) {
                if (defaultProperty != null) {
                    return defaultProperty;
                }

                throw new PropertyIsInvalid(name);
            } else if (dimensionPropertiesWithMaxWeight.size() > 1) {
                throw new PropertyIsInvalid(name);
            } else {
                return dimensionPropertiesWithMaxWeight.get(0);
            }
        }
    }

    private int calculatePropertyWeight(PointProperty dimensionProperty, Point point) {
        int weight = 0;

        Map<Dimension, DimensionValue> location = dimensionProperty.getPoint().getLocation();

        for (Dimension dimension : location.keySet()) {
            DimensionValue v1 = point.getLocation().get(dimension);
            DimensionValue v2 = location.get(dimension);

            if (v2 != null) {
                if (v1.equals(v2)) {
                    weight++;
                } else {
                    return -1;
                }
            }
        }

        return weight;
    }
}
