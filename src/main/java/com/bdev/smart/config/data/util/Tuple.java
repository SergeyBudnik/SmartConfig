package com.bdev.smart.config.data.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Tuple<A, B> {
    private A a;
    private B b;
}
