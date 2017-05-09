package com.bdev.smart.config.exceptions;

public class DimensionDoesNotExistException extends RuntimeException {
    public DimensionDoesNotExistException(
            String propertyName,
            String dimensionName
    ) {
        super(
            String.format(
                "Dimension '%s' does not exist. Found in '%s' property.",
                dimensionName,
                propertyName
            )
        );
    }
}
