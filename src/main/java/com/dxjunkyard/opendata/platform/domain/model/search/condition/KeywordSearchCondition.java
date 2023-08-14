package com.dxjunkyard.opendata.platform.domain.model.search.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class KeywordSearchCondition {

    private final Set<String> keywordSet;

    public static KeywordSearchCondition of(final String queries) {
        return new KeywordSearchCondition(Stream.of(StringUtils
            .split(StringUtils.defaultString(queries), StringUtils.SPACE)).collect(Collectors.toUnmodifiableSet()));
    }

    Set<String> getKeywordSet() {
        return keywordSet;
    }
}
