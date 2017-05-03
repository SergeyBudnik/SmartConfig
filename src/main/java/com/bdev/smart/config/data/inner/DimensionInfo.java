package com.bdev.smart.config.data.inner;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class DimensionInfo {
    private Set<String> dimensions = new HashSet<>();
}
