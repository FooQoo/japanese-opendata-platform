package com.dxjunkyard.opendata.platform.domain.model.opendata.tokyo;

import com.dxjunkyard.opendata.platform.domain.model.opendata.Dataset;
import com.dxjunkyard.opendata.platform.domain.model.opendata.DatasetFile;
import java.util.List;
import lombok.*;

public class TokyoDataset extends Dataset {

    @Builder
    public TokyoDataset(
        final String title,
        final String description,
        final String datasetUrl,
        final String maintainer,
        final String license,
        final List<TokyoDatasetFile> files) {
        super(
            title,
            description,
            datasetUrl,
            maintainer,
            license,
            files.stream().map(file -> (DatasetFile) file).toList());
    }
}
