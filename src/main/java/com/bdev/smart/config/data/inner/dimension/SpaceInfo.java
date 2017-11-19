package com.bdev.smart.config.data.inner.dimension;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class SpaceInfo {
    private final Space space;
    private final Set<Point> points;
}
