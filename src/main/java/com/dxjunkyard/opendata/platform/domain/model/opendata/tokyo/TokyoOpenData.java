package com.dxjunkyard.opendata.platform.domain.model.opendata.tokyo;

import com.dxjunkyard.opendata.platform.domain.model.opendata.Dataset;
import com.dxjunkyard.opendata.platform.domain.model.opendata.OpenData;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.util.List;

public class TokyoOpenData extends OpenData {

    @Builder
    public TokyoOpenData(
        @NonNull final Integer total,
        @NonNull final List<TokyoDataset> dataset) {
        super(total, dataset.stream().map(data -> (Dataset) data).toList());
    }
}
