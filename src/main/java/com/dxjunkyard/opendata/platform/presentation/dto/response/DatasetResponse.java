package com.dxjunkyard.opendata.platform.presentation.dto.response;

import com.dxjunkyard.opendata.platform.domain.model.opendata.Dataset;
import com.dxjunkyard.opendata.platform.domain.model.opendata.tokyo.TokyoDataset;
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

    public static DatasetResponse from(final Dataset dataset, @Nullable final List<DatasetFileResponse> overriddenFiles) {
        if (dataset instanceof TokyoDataset tokyoDataset) {

            // 上書き用のオープンデータがあればそれを使う
            final var filesResponse = Objects.nonNull(overriddenFiles)
                ? overriddenFiles
                : tokyoDataset.getFiles().stream().map(DatasetFileResponse::from).toList();

            return DatasetResponse.builder()
                .title(tokyoDataset.getTitle())
                .description(tokyoDataset.getDescription())
                .datasetUrl(tokyoDataset.getDatasetUrl())
                .maintainer(tokyoDataset.getMaintainer())
                .license(tokyoDataset.getLicense())
                .files(filesResponse)
                .build();
        }

        throw new IllegalArgumentException("Unknown dataset type");
    }

}
