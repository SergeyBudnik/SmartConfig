package com.bdev.smart.config.data.inner.property;

import com.bdev.smart.config.data.util.Tuple;
import com.bdev.smart.config.exceptions.PropertyIsInvalid;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Property {
    private String name;
    private PropertyType type;
    private DefaultProperty defaultProperty;
    private Collection<DimensionProperty> dimensionsProperty = new ArrayList<>();
    private boolean readProtected = true;
    private boolean overrideProtected = true;

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

    public void addDimensionProperty(DimensionProperty dimensionProperty) {
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
    public ConditionalProperty getMostSuitableProperty(
            List<Tuple<String, String>> dimensionsValues
    ) {
        List<Tuple<Integer, DimensionProperty>> weightedDimensionsPropertyInfo =
                new ArrayList<>();

        if (dimensionsProperty.size() == 0) {
            if (defaultProperty == null) {
                throw new PropertyIsInvalid(name);
            }

            return defaultProperty;
        } else {
            for (DimensionProperty dimensionProperty : dimensionsProperty) {
                weightedDimensionsPropertyInfo.add(
                        new Tuple<>(
                                calculatePropertyWeight(dimensionProperty, dimensionsValues),
                                dimensionProperty
                        )
                );
            }

            int maxWeight = weightedDimensionsPropertyInfo
                    .stream()
                    .mapToInt(Tuple::getA)
                    .max()
                    .orElseThrow(RuntimeException::new);

            List<DimensionProperty> dimensionPropertiesWithMaxWeight = weightedDimensionsPropertyInfo
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

    private int calculatePropertyWeight(
            DimensionProperty dimensionProperty,
            List<Tuple<String, String>> dimensionsValues
    ) {
        int weight = 0;

        for (String dimensionName : dimensionProperty.getDimensions().keySet()) {
            String dimensionValue = dimensionProperty.getDimensions().get(dimensionName);

            boolean dimensionSuitable = dimensionsValues
                    .stream()
                    .map(Tuple::getB)
                    .anyMatch(it -> it.equals(dimensionValue));

            if (!dimensionSuitable) {
                return -1;
            }
        }

        for (Tuple<String, String> dimensionValueInfo : dimensionsValues) {
            String dimensionValue = dimensionValueInfo.getB();

            if (dimensionProperty.getDimensions().containsValue(dimensionValue)) {
                weight++;
            }
        }

        return weight;
    }
}
