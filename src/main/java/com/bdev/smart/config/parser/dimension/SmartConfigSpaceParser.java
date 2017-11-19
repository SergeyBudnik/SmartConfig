package com.bdev.smart.config.parser.dimension;

import com.bdev.smart.config.data.inner.dimension.*;
import com.bdev.smart.config.reader.SmartConfigReader;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class SmartConfigSpaceParser {
    private static final String SPACE_KEYWORD = "space";
    private static final String POINTS_KEYWORD = "points";

    public static SpaceInfo parse(String fileName) {
        Space space = parseSpace(fileName);
        Set<Point> points = parsePoints(fileName, space);

        return new SpaceInfo(space, points);
    }

    private static Space parseSpace(String fileName) {
        Map<String, Object> dimensionsProperties = SmartConfigReader
                .read(fileName)
                .stream()
                .filter(it -> it.getKey().startsWith(SPACE_KEYWORD + "."))
                .collect(Collectors.toMap(
                        it -> parseDimensionName(it.getKey()),
                        it -> it.getValue().unwrapped()
                ));

        if (dimensionsProperties.isEmpty()) {
            throw new RuntimeException("At least one dimension must exist");
        }

        Space space = new Space();

        dimensionsProperties.forEach((dimensionName, dimensionValuesObject) -> {
            if (!(dimensionValuesObject instanceof List)) {
                throw new RuntimeException("Dimension schema should be an array");
            }

            List<DimensionValue> dimensionValues = ((List<Object>) dimensionValuesObject)
                    .stream()
                    .map(it -> new DimensionValue((String) it))
                    .collect(Collectors.toList());

            if (dimensionValues.isEmpty()) {
                throw new RuntimeException("Dimension should have at least one value");
            }

            Dimension dimension = new Dimension(dimensionName); {
                dimensionValues.forEach(dimension::addValue);
            }

            space.addDimension(dimension);
        });

        return space;
    }

    private static Set<Point> parsePoints(String fileName, Space space) {
        Optional<Object> pointsProperty = SmartConfigReader
                .read(fileName)
                .stream()
                .filter(it -> it.getKey().startsWith(POINTS_KEYWORD))
                .map(it -> it.getValue().unwrapped())
                .findAny();

        if (!pointsProperty.isPresent()) {
            throw new RuntimeException("Points schema should exist");
        }

        if (!(pointsProperty.get() instanceof List)) {
            throw new RuntimeException("Points schema should be an array");
        }

        Set<Point> points = new HashSet<>();

        for (Object pointProperty : (List) pointsProperty.get()) {
            if (!(pointProperty instanceof Map)) {
                throw new RuntimeException("Point schema should be a map");
            }

            Point point = new Point();

            ((Map<String, String>) pointProperty).forEach((dimensionName, dimensionValueName) -> {
                Dimension dimension = space
                        .getDimensionByName(dimensionName)
                        .orElseThrow(() -> new RuntimeException(format("Dimension '%s' does not exist", dimensionName)));

                DimensionValue dimensionValue = dimension
                        .getValue(dimensionValueName)
                        .orElseThrow(() -> new RuntimeException(format("Dimension value '%s' does not exist in dimension '%s'",
                                dimensionValueName, dimensionName))
                        );

                point.addCoordinate(dimension, dimensionValue);
            });

            if (!point.isCompleted(space)) {
                throw new RuntimeException(
                        format("Point \n%s\nis not completed\n", point.getDescription())
                );
            }

            points.add(point);
        }

        return points;
    }

    private static String parseDimensionName(String rawDimensionName) {
        String [] rawDimensionNameParts = rawDimensionName.split("\\.");

        if (rawDimensionNameParts.length != 2) {
            throw new RuntimeException("Incorrect dimension name format");
        }

        return rawDimensionNameParts[1];
    }
}
