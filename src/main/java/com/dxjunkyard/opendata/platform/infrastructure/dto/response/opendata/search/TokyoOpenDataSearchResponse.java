package com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.search;

import com.dxjunkyard.opendata.platform.domain.model.opendata.tokyo.TokyoDataset;
import com.dxjunkyard.opendata.platform.domain.model.opendata.tokyo.TokyoDatasetFile;
import com.dxjunkyard.opendata.platform.domain.model.opendata.tokyo.TokyoOpenData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record TokyoOpenDataSearchResponse(
    SearchResultResponse result
) implements OpenDataSearchResponse {

    @NonNull
    public TokyoOpenData toTokyoOpenData() {
        return TokyoOpenData.builder()
            .total(result.count())
            .dataset(result.toTokyoDataset())
            .build();
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SearchResultResponse(
        Integer count,
        List<ResultResponse> results
    ) {
        public List<TokyoDataset> toTokyoDataset() {
            return results.stream()
                .map(ResultResponse::toDataset)
                .toList();
        }
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResultResponse(
        String title,
        String name,
        String notes,
        String url,
        String type,
        String metadataModified,
        String metadataCreated,
        String maintainer,
        String licenseTitle,
        List<ResourceResponse> resources,
        List<GroupResponse> groups,
        OrganizationResponse organization
    ) {
        public LocalDateTime getMetadataModified() {
            return LocalDateTime.parse(metadataModified);
        }

        public LocalDateTime getMetadataCreated() {
            return LocalDateTime.parse(metadataCreated);
        }

        @NonNull
        public TokyoDataset toDataset() {
            return TokyoDataset.builder()
                .title(title)
                .description(notes)
                .datasetUrl(Optional.ofNullable(url).filter(StringUtils::isNotBlank).orElse(null))
                .maintainer(maintainer)
                .license(licenseTitle)
                .files(resources.stream()
                    .map(ResourceResponse::toDatasetFile)
                    .toList())
                .build();
        }
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResourceResponse(
        String id,
        String name,
        String description,
        String format,
        String url,
        String lastModified,
        String created
    ) {

        public TokyoDatasetFile toDatasetFile() {
            return TokyoDatasetFile.builder()
                .title(name)
                .description(description)
                .format(format)
                .lastModified(Optional.ofNullable(lastModified)
                    .map(datetime -> LocalDateTime.parse(datetime, DateTimeFormatter.ISO_DATE_TIME))
                    .orElse(null))
                .url(url)
                .build();
        }
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GroupResponse(
        String id,
        String name,
        String title
    ) {
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OrganizationResponse(
        String id,
        String name,
        String title
    ) {
    }
}
