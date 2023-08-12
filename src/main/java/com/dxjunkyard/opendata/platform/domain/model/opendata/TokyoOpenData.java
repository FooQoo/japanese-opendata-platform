package com.dxjunkyard.opendata.platform.domain.model.opendata;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Builder(toBuilder = true)
@ToString
public class TokyoOpenData implements OpenData {

    @NonNull
    private final Integer total;

    @NonNull
    private final List<TokyoDataset> dataset;
}
