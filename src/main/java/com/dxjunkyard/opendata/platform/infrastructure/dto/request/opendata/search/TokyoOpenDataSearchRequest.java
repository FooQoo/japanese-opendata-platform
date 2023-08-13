package com.dxjunkyard.opendata.platform.infrastructure.dto.request.opendata.search;

import com.dxjunkyard.opendata.platform.domain.model.search.SearchCondition;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <a href="https://docs.ckan.org/en/2.8/api/#ckan.logic.action.get.package_search">ckan.logic.action.get.package_search</a>
 */
@Builder
public record TokyoOpenDataSearchRequest(
    @NonNull Integer start,
    @NonNull Integer rows,
    @Nullable String q
) implements OpenDataSearchRequest, Serializable {

    private static final Integer DEFAULT_ROWS = 5;

    @NonNull
    static public TokyoOpenDataSearchRequest from(final SearchCondition searchCondition) {

        final String searchKeyword = searchCondition.getKeyword();

        final String organization = searchCondition.getOrganizationIdSet().stream()
            .map(organizationId -> "organization:" + organizationId.toString())
            .collect(Collectors.joining(" OR "));

        final String groups = searchCondition.getCategoryIdSet().stream()
            .map(categoryId -> "groups:" + categoryId.toString())
            .collect(Collectors.joining(" AND "));

        final String resFormat = searchCondition.getFormatSet().stream()
            .map(openDataFormat -> "res_format:" + openDataFormat.toString())
            .collect(Collectors.joining(" AND "));

        final String q = Stream.of(
                Optional.ofNullable(searchKeyword).filter(StringUtils::isNotBlank)
                    .orElse(""),
                Optional.of(organization).filter(StringUtils::isNotBlank)
                    .map(value -> "(" + value + ")").orElse(""),
                Optional.of(groups).filter(StringUtils::isNotBlank)
                    .map(value -> "(" + value + ")").orElse(""),
                Optional.of(resFormat).filter(StringUtils::isNotBlank)
                    .map(value -> "(" + value + ")").orElse("")
            )
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.joining(" AND ")
            );

        return TokyoOpenDataSearchRequest.builder()
            .q(Optional.of(q).filter(StringUtils::isNotBlank).orElse(null))
            .start(getStart(searchCondition.getPage()))
            .rows(DEFAULT_ROWS)
            .build();
    }

    @NonNull
    private static Integer getStart(@NonNull final Integer page) {
        return (page - 1) * DEFAULT_ROWS;
    }
}
