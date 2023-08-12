package com.dxjunkyard.opendata.platform.presentation.dto.response;

import com.dxjunkyard.opendata.platform.domain.model.search.SearchCondition;
import org.springframework.lang.NonNull;

import java.io.Serializable;

public record SearchConditionResponse(
    String keyword,
    String organization,
    String category,
    String format
) implements Serializable {

    @NonNull
    public static SearchConditionResponse empty() {
        return new SearchConditionResponse(null, null, null, null);
    }

    @NonNull
    public static SearchConditionResponse from(final SearchCondition searchCondition) {
        return new SearchConditionResponse(
            searchCondition.getKeyword(),
            searchCondition.getOrganization(),
            searchCondition.getCategory(),
            searchCondition.getFormat()
        );
    }
}
