package com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.category;

import com.dxjunkyard.opendata.platform.domain.model.search.CategoryNameToIdConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.NonNull;

import java.util.List;


@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record TokyoOpenDataCategoryResponse(
    List<ResultResponse> result
) implements OpenDataCategoryResponse {

    @NonNull
    public CategoryNameToIdConverter convertCategory() {
        final CategoryNameToIdConverter converter = CategoryNameToIdConverter.init();

        result().forEach(result -> converter.add(result.title(), result.name()));

        return converter.freeze();
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResultResponse(
        String title,
        String name
    ) {
    }
}
