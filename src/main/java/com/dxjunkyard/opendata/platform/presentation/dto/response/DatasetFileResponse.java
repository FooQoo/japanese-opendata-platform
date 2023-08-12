package com.dxjunkyard.opendata.platform.presentation.dto.response;

import com.dxjunkyard.opendata.platform.domain.model.opendata.DatasetFile;
import com.dxjunkyard.opendata.platform.domain.model.opendata.TokyoDatasetFile;
import lombok.Builder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Builder
public record DatasetFileResponse(
    @NonNull String title,
    @NonNull
    String description,
    @Nullable String format,
    @NonNull String url
) implements Serializable {

    public static DatasetFileResponse from(final DatasetFile datasetFile) {
        if (datasetFile instanceof TokyoDatasetFile tokyoDatasetFile) {
            return DatasetFileResponse.builder()
                .title(tokyoDatasetFile.getTitle())
                .description(tokyoDatasetFile.getDescription())
                .format(tokyoDatasetFile.getFormat())
                .url(tokyoDatasetFile.getUrl())
                .build();
        }

        throw new IllegalArgumentException("Unknown dataset file type");
    }
}
