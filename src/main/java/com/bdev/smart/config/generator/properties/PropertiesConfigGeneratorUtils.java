package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.DimensionInfo;
import com.bdev.smart.config.data.util.Tuple;

import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

class PropertiesConfigGeneratorUtils {
    static void gatherDimensionsMultiplication(
            ConfigInfo configInfo,
            List<String> dimensionNames,
            int dimensionIndex,
            Stack<Tuple<String, String>> dimensionValues,
            Consumer<Stack<Tuple<String, String>>> action
    ) {
        if (dimensionIndex == dimensionNames.size()) {
            action.accept(dimensionValues);
        } else {
            String dimensionName = dimensionNames.get(dimensionIndex);

            DimensionInfo dimensionInfo = configInfo.getDimensions().get(dimensionName);

            for (String dimensionValue : dimensionInfo.getDimensions()) {
                dimensionValues.push(new Tuple<>(dimensionName, dimensionValue));

                gatherDimensionsMultiplication(
                        configInfo,
                        dimensionNames,
                        dimensionIndex + 1,
                        dimensionValues,
                        action
                );

                dimensionValues.pop();
            }
        }
    }
}
