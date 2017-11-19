package com.bdev.smart.config.data.inner.dimension;

import lombok.Getter;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.String.format;

public class Point {
    @Getter private Map<Dimension, DimensionValue> location = new TreeMap<>(
            Comparator.comparing(Dimension::getName)
    );

    public String getName() {
        StringBuilder sb = new StringBuilder();

        for (Dimension dimension : location.keySet()) {
            String dimensionValueName = location.get(dimension).getName();

            sb.append(dimensionValueName.substring(0, 1).toUpperCase());
            sb.append(dimensionValueName.substring(1).toLowerCase());
        }

        return sb.toString();
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");

        for (Dimension dimension : location.keySet()) {
            sb.append(dimension.getName());
            sb.append(": ");
            sb.append(location.get(dimension).getName());
            sb.append("\n");
        }

        sb.append("}");

        return sb.toString();
    }

    public boolean isCompleted(Space space) {
        for (Dimension dimension : space.getDimensions()) {
            if (!location.containsKey(dimension)) {
                return false;
            }
        }

        return true;
    }

    public void addCoordinate(Dimension dimension, DimensionValue dimensionValue) {
        if (location.containsKey(dimension)) {
            throw new RuntimeException(format("Point already contains dimension '%s'", dimension.getName()));
        }

        location.put(dimension, dimensionValue);
    }

    public boolean containsCoordinate(String dimension, String dimensionValue) {
        return
                location.containsKey(new Dimension(dimension)) &&
                location.get(new Dimension(dimension)).equals(new DimensionValue(dimensionValue));
    }
}
