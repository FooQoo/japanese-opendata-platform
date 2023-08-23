package com.dxjunkyard.opendata.platform.domain.model.opendata;

import com.dxjunkyard.opendata.platform.domain.model.search.condition.SearchCondition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@EqualsAndHashCode
public abstract class DatasetFile {

    @NonNull
    @Getter
    protected final String title;
    @NonNull
    @Getter
    protected final String description;
    @Nullable
    @Getter
    protected final String format;
    @Nullable
    protected final LocalDateTime lastModified;
    @NonNull
    @Getter
    protected final String url;

    @Nullable
    public Long getLastModifiedTimestamp() {
        return Optional.ofNullable(lastModified)
            .map(timestamp -> timestamp.toEpochSecond(java.time.ZoneOffset.of("+09:00")))
            .orElse(null);
    }

    public boolean isMatched(final SearchCondition searchCondition) {

        // キーワードが存在しない場合は、全てのデータをマッチさせる
        if (!searchCondition.existsKeyword()) {
            return true;
        }

        final boolean isMatchedTitle = StringUtils.containsAny(title, searchCondition.getAllQuerySet().toArray(String[]::new));
        final boolean isMatchedDescription = StringUtils.containsAny(description, searchCondition.getAllQuerySet().toArray(String[]::new));

        return isMatchedTitle || isMatchedDescription;
    }
}
