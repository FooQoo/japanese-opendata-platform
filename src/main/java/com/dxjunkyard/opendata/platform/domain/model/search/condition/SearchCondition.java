package com.dxjunkyard.opendata.platform.domain.model.search.condition;

import com.dxjunkyard.opendata.platform.domain.model.opendata.OpenDataFormat;
import com.dxjunkyard.opendata.platform.domain.model.search.Prefecture;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
@Builder
public class SearchCondition {

    @NonNull
    private final Integer page;

    @NonNull
    private final KeywordSearchCondition keywordSearchCondition;

    @NonNull
    private final Prefecture prefecture;

    @NonNull
    private final OrganizationSearchCondition organizationSearchCondition;

    @NonNull
    private final CategorySearchCondition categorySearchCondition;

    @NonNull
    private final Set<OpenDataFormat> formatSet;


    public boolean existsKeyword() {
        return CollectionUtils.isNotEmpty(getAllQuerySet());
    }

    public Set<String> getAllQuerySet() {
        return Stream.of(
            keywordSearchCondition.getKeywordSet(),
            organizationSearchCondition.getOrganizationQuery(),
            categorySearchCondition.getCategoryQuery()
        ).flatMap(Collection::stream).collect(Collectors.toUnmodifiableSet());
    }

    public String getAllQuery() {
        return String.join(" ", getAllQuerySet());
    }

    @Nullable
    public String getFormat() {

        if (formatSet.isEmpty()) {
            return null;
        }

        return String.join(" ", formatSet.stream()
            .map(OpenDataFormat::getValue)
            .collect(Collectors.toUnmodifiableSet()));
    }
}
