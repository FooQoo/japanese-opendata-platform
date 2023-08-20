package com.dxjunkyard.opendata.platform.domain.model.opendata.tokyo;

import com.dxjunkyard.opendata.platform.domain.model.opendata.DatasetFile;
import com.dxjunkyard.opendata.platform.domain.model.search.condition.SearchCondition;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Optional;


public class TokyoDatasetFile extends DatasetFile {

    @Builder
    public TokyoDatasetFile(
        final String title,
        final String description,
        final String format,
        final LocalDateTime lastModified,
        final String url) {
        super(title, description, format, lastModified, url);
    }
}
