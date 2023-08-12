package com.dxjunkyard.opendata.platform.domain.model.opendata;

import com.dxjunkyard.opendata.platform.domain.model.search.SearchCondition;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Builder
public class TokyoDatasetFile implements DatasetFile {

    @NonNull
    private final String title;
    @NonNull
    private final String description;
    @NonNull
    private final String format;
    @Nullable
    private final LocalDateTime lastModified;
    @NonNull
    private final String url;

    @Override
    @NonNull
    public String getTitle() {
        return title;
    }

    @Override
    @NonNull
    public String getDescription() {
        return description;
    }

    @Override
    @Nullable
    public String getFormat() {
        return format;
    }

    @Override
    @NonNull
    public String getUrl() {
        return url;
    }

    @Override
    @Nullable
    public Long getLastModifiedTimestamp() {
        return Optional.ofNullable(lastModified)
            .map(timestamp -> timestamp.toEpochSecond(java.time.ZoneOffset.of("+09:00")))
            .orElse(null);
    }

    @Override
    public boolean isMatched(final SearchCondition searchCondition) {

        // キーワードが存在しない場合は、全てのデータをマッチさせる
        if (!searchCondition.existsKeyword()) {
            return true;
        }

        final boolean isMatchedTitle = StringUtils.containsAny(title, searchCondition.getKeywordSet().toArray(String[]::new));
        final boolean isMatchedDescription = StringUtils.containsAny(description, searchCondition.getKeywordSet().toArray(String[]::new));

        return isMatchedTitle || isMatchedDescription;
    }


}
