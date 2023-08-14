package com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.organization;

import com.dxjunkyard.opendata.platform.domain.model.search.OrganizationNameToIdConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.NonNull;

import java.util.List;


@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record TokyoOpenDataOrganizationResponse(
    List<ResultResponse> result
) implements OpenDataOrganizationResponse {

    @NonNull
    public OrganizationNameToIdConverter convertOrganization() {
        final OrganizationNameToIdConverter converter = OrganizationNameToIdConverter.init();

        result().forEach(result -> converter.add(result.title(), result.name()));

        return converter;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResultResponse(
        String title,
        String name
    ) {
    }
}
