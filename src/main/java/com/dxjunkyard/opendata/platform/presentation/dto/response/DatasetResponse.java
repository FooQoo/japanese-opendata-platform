package com.dxjunkyard.opendata.platform.presentation.dto.response;

import com.dxjunkyard.opendata.platform.domain.model.opendata.Dataset;
import lombok.Builder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Builder
public record DatasetResponse(
    @NonNull String title,
    @Nullable String description,
    @Nullable String datasetUrl,
    @NonNull String maintainer,
    @NonNull String license,
    @NonNull List<DatasetFileResponse> files) implements Serializable {

    public static DatasetResponse from(final Dataset dataset, @Nullable final List<DatasetFileResponse> overriddenFiles, final String titleEn) {
        // 上書き用のオープンデータがあればそれを使う
        final var filesResponse = Objects.nonNull(overriddenFiles)
            ? overriddenFiles
            : dataset.getFiles().stream().map(DatasetFileResponse::from).toList();

        return DatasetResponse.builder()
            .title(dataset.getTitle())
            .description(dataset.getDescription())
            .datasetUrl(dataset.getDatasetUrl())
            .maintainer(dataset.getMaintainer())
            .license(dataset.getLicense())
            .files(filesResponse)
            .build();
    }

}
