package com.dxjunkyard.opendata.platform.domain.model.opendata;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Builder
@ToString
public class TokyoDataset implements Dataset {
    @NonNull
    private final String title;
    @Nullable
    private final String description;
    @Nullable
    private final String datasetUrl;
    @NonNull
    private final String maintainer;
    @NonNull
    private final String license;
    @NonNull
    private final List<TokyoDatasetFile> files;

    @RequiredArgsConstructor
    @Getter
    @Builder
    public static class TokyoDatasetFile {
        @NonNull
        private final String title;
        @NonNull
        private final String format;
        @NonNull
        private final String url;
    }
}
