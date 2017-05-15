package com.bdev.smart.config.data.inner.property;

import com.bdev.smart.config.data.util.Tuple;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Property {
    private PropertyType type;
    private DefaultProperty defaultProperty;
    private Collection<DimensionProperty> dimensionsProperty = new ArrayList<>();

    public void setDefaultProperty(DefaultProperty defaultProperty) {
        if (this.defaultProperty != null) {
            throw new RuntimeException();
        }

        if (type == null) {
            type = defaultProperty.getType();
        } else {
            if (type != defaultProperty.getType()) {
                throw new RuntimeException();
            }
        }

        this.defaultProperty = defaultProperty;
    }

    public void addDimensionProperty(DimensionProperty dimensionProperty) {
        if (type == null) {
            type = dimensionProperty.getType();
        } else {
            if (type != dimensionProperty.getType()) {
                throw new RuntimeException();
            }
        }

        dimensionsProperty.add(dimensionProperty);
    }

    // ToDo: Test
    public DimensionProperty getMostSuitableProperty(
            List<Tuple<String, String>> dimensionsValues
    ) {
        List<Tuple<Integer, DimensionProperty>> weightedDimensionsPropertyInfo =
                new ArrayList<>();

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
                .filter(it -> it.getA() == maxWeight)
                .map(Tuple::getB)
                .collect(Collectors.toList());

        if (dimensionPropertiesWithMaxWeight.size() <= 0) {
            dimensionsValues
                    .stream()
                    .map(Tuple::getB)
                    .forEach(System.out::println);

            throw new RuntimeException("No suitable");
        } else if (dimensionPropertiesWithMaxWeight.size() > 1) {
            throw new RuntimeException("More than one");
        } else {
            return dimensionPropertiesWithMaxWeight.get(0);
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
