package com.bdev.smart.config.data.inner;

import com.bdev.smart.config.data.util.Tuple;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PropertyInfo {
    private PropertyType type;
    private DimensionPropertyInfo defaultPropertyInfo;
    private Collection<DimensionPropertyInfo> dimensionsPropertyInfo;

    // ToDo: Test
    public DimensionPropertyInfo getMostSuitableProperty(
            List<Tuple<String, String>> dimensionsValues
    ) {
        List<Tuple<Integer, DimensionPropertyInfo>> weightedDimensionsPropertyInfo =
                new ArrayList<>();

        for (DimensionPropertyInfo dimensionPropertyInfo : dimensionsPropertyInfo) {
            weightedDimensionsPropertyInfo.add(
                    new Tuple<>(
                            calculatePropertyWeight(dimensionPropertyInfo, dimensionsValues),
                            dimensionPropertyInfo
                    )
            );
        }

        int maxWeight = weightedDimensionsPropertyInfo
                .stream()
                .mapToInt(Tuple::getA)
                .max()
                .orElseThrow(RuntimeException::new);

        List<DimensionPropertyInfo> dimensionPropertiesWithMaxWeight = weightedDimensionsPropertyInfo
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
            DimensionPropertyInfo dimensionPropertyInfo,
            List<Tuple<String, String>> dimensionsValues
    ) {
        int weight = 0;

        for (String dimension : dimensionPropertyInfo.getDimensions()) {
            boolean dimensionSuitable = dimensionsValues
                    .stream()
                    .map(Tuple::getB)
                    .anyMatch(it -> it.equals(dimension));

            if (!dimensionSuitable) {
                return -1;
            }
        }

        for (Tuple<String, String> dimensionValueInfo : dimensionsValues) {
            String dimensionValue = dimensionValueInfo.getB();

            if (dimensionPropertyInfo.getDimensions().contains(dimensionValue)) {
                weight++;
            }
        }

        return weight;
    }
}
