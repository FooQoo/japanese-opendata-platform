package com.dxjunkyard.opendata.platform.presentation.dto.factory;

import com.dxjunkyard.opendata.platform.domain.model.opendata.CategoryId;
import com.dxjunkyard.opendata.platform.domain.model.opendata.OpenData;
import com.dxjunkyard.opendata.platform.domain.model.opendata.OrganizationId;
import com.dxjunkyard.opendata.platform.domain.model.opendata.TokyoOpenData;
import com.dxjunkyard.opendata.platform.domain.model.search.CategoryNameToIdConverter;
import com.dxjunkyard.opendata.platform.domain.model.search.OrganizationNameToIdConverter;
import com.dxjunkyard.opendata.platform.domain.model.search.Prefecture;
import com.dxjunkyard.opendata.platform.domain.model.search.SearchCondition;
import com.dxjunkyard.opendata.platform.domain.repository.opendata.OpenDataRepository;
import com.dxjunkyard.opendata.platform.domain.service.FilterDatasetFileDomainService;
import com.dxjunkyard.opendata.platform.domain.service.UrlBuilderDomainService;
import com.dxjunkyard.opendata.platform.presentation.dto.request.OpenDataSearcherRequest;
import com.dxjunkyard.opendata.platform.presentation.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class OpenDataSearcherFactory {

    private final OpenDataRepository openDataRepository;

    private final UrlBuilderDomainService urlBuilderDomainService;

    private final FilterDatasetFileDomainService filterDatasetFileDomainService;

    @NonNull
    public Mono<SearchCondition> build(final OpenDataSearcherRequest request) {

        final Mono<OrganizationNameToIdConverter> organizationNameToIdConverterMono = openDataRepository
            .fetchOrganization();

        final Mono<CategoryNameToIdConverter> categoryNameToIdConverterMono = openDataRepository
            .fetchCategory();

        return Mono.zip(organizationNameToIdConverterMono, categoryNameToIdConverterMono)
            .map(tuple -> {
                final OrganizationNameToIdConverter organizationNameToIdConverter = tuple.getT1();
                final CategoryNameToIdConverter categoryNameToIdConverter = tuple.getT2();

                final Map<String, OrganizationId> organizationIds = request.organizationSet().stream()
                    .filter(organizationNameToIdConverter::contains)
                    .collect(Collectors.toUnmodifiableMap(
                        key -> key,
                        organizationNameToIdConverter::convert));

                final List<String> organizationQueries = request.organizationSet().stream()
                    .filter(organizationName -> !organizationNameToIdConverter.contains(organizationName))
                    .toList();

                final Map<String, CategoryId> categoryIds = request.categorySet().stream()
                    .filter(categoryNameToIdConverter::contains)
                    .collect(Collectors.toUnmodifiableMap(
                        key -> key,
                        categoryNameToIdConverter::convert));

                final List<String> categoryQueries = request.categorySet().stream()
                    .filter(categoryName -> !categoryNameToIdConverter.contains(categoryName))
                    .toList();

                final Set<String> additionalQueryList = Stream.of(
                    Stream.of(StringUtils
                        .split(StringUtils.defaultString(request.keyword()), StringUtils.SPACE)).toList(),
                    organizationQueries,
                    categoryQueries
                ).flatMap(List::stream).collect(Collectors.toUnmodifiableSet());

                return SearchCondition.builder()
                    .page(request.page())
                    .keywordSet(CollectionUtils.isNotEmpty(additionalQueryList)
                        ? additionalQueryList
                        : Set.of())
                    // 現時点は東京のみ対応
                    .prefecture(Prefecture.TOKYO)
                    .organizationMap(organizationIds)
                    .categoryMap(categoryIds)
                    .formatSet(request.formatSet())
                    .build();
            });
    }

    @NonNull
    public OpenDataSearcherResponse build(final OpenData openData, final SearchCondition searchCondition) {
        if (openData instanceof TokyoOpenData tokyoOpenData) {
            final List<DatasetResponse> datasetResponse = tokyoOpenData.getDataset().stream()
                .map(dataset -> {
                    final var datasetFileResponse = filterDatasetFileDomainService.filter(dataset.getDatasetFile(), searchCondition)
                        .stream()
                        .map(datasetFile -> DatasetFileResponse.builder()
                            .title(datasetFile.getTitle())
                            .description(datasetFile.getDescription())
                            .format(datasetFile.getFormat())
                            .url(datasetFile.getUrl())
                            .build())
                        .toList();

                    return DatasetResponse.builder()
                        .title(dataset.getTitle())
                        .description(dataset.getDescription())
                        .datasetUrl(dataset.getDatasetUrl())
                        .maintainer(dataset.getMaintainer())
                        .license(dataset.getLicense())
                        .files(datasetFileResponse)
                        .build();
                }).toList();

            return OpenDataSearcherResponse.builder()
                .searchResultInfo(new SearchResultInfoResponse(tokyoOpenData.getTotal()))
                .dataset(datasetResponse)
                .searchCondition(SearchConditionResponse.from(searchCondition))
                .showMoreUrl(urlBuilderDomainService.build(searchCondition))
                .build();
        }

        return OpenDataSearcherResponse.empty();
    }
}
