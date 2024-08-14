package com.caju_authorization.constant;

import java.util.Arrays;
import java.util.List;

public enum MccEnum {
    FOOD("5411", "5412"),
    MEAL("5811", "5812"),
    DEFAULT("default");

    private final List<String> codes;

    MccEnum(String... codes) {
        this.codes = Arrays.asList(codes);
    }

    public static MccEnum fromMcc(String mcc) {
        return Arrays.stream(values())
                .filter(type -> type.codes.contains(mcc))
                .findFirst()
                .orElse(DEFAULT);
    }
}
