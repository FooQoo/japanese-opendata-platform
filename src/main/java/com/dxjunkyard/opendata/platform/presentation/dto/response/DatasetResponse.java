package com.dxjunkyard.opendata.platform.presentation.dto.response;

import com.dxjunkyard.opendata.platform.domain.model.opendata.Dataset;
import com.dxjunkyard.opendata.platform.domain.model.opendata.TokyoDataset;
import lombok.Builder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

@Builder
public record DatasetResponse(
    @NonNull String title,
    @Nullable String description,
    @Nullable String datasetUrl,

    @NonNull String maintainer,
    @NonNull String license,

    @NonNull List<DatasetFileResponse> files) {

    public static DatasetResponse from(final Dataset dataset) {
        if (dataset instanceof TokyoDataset tokyoDataset) {
            return DatasetResponse.builder()
                .title(tokyoDataset.getTitle())
                .description(tokyoDataset.getDescription())
                .datasetUrl(tokyoDataset.getDatasetUrl())
                .maintainer(tokyoDataset.getMaintainer())
                .license(tokyoDataset.getLicense())
                .files(tokyoDataset.getFiles().stream()
                    .map(DatasetFileResponse::from)
                    .toList())
                .build();
        }

        throw new IllegalArgumentException("Unknown dataset type");
    }

}
