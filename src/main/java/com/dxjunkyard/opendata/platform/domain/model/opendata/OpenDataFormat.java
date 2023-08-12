package com.dxjunkyard.opendata.platform.domain.model.opendata;

import org.springframework.lang.NonNull;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum OpenDataFormat {
    CSV,
    JSON,
    ;

    @NonNull
    public static Set<String> getValues() {
        return Stream.of(OpenDataFormat.values())
            .map(Enum::name)
            .collect(Collectors.toUnmodifiableSet());
    }

    @NonNull
    public OpenDataFormat fromString(@NonNull final String format) {
        return OpenDataFormat.valueOf(format);
    }
}
