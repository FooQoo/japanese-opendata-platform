package com.dxjunkyard.opendata.platform.domain.model.opendata.tokyo;

import com.dxjunkyard.opendata.platform.domain.model.opendata.Dataset;
import com.dxjunkyard.opendata.platform.domain.model.opendata.DatasetFile;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Builder(toBuilder = true)
@ToString
public class TokyoDataset implements Dataset {
    // 以下の値を超えたら、ファイルが多すぎると判断する
    private static final int TOO_MANY_FILES = 3;
    
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

    @Override
    public List<DatasetFile> getDatasetFile() {
        return files.stream().map(file -> (DatasetFile) file).toList();
    }

    @Override
    public boolean isTooManyFiles() {
        return files.size() > TOO_MANY_FILES;
    }
}
