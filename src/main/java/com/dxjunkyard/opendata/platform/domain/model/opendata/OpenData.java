package com.dxjunkyard.opendata.platform.domain.model.opendata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

@RequiredArgsConstructor
@Getter
public abstract class OpenData {
    @NonNull
    protected final Integer total;

    @NonNull
    protected final List<Dataset> dataset;
}
