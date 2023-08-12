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
import com.dxjunkyard.opendata.platform.domain.service.UrlBuilderDomainService;
import com.dxjunkyard.opendata.platform.presentation.dto.request.OpenDataSearcherRequest;
import com.dxjunkyard.opendata.platform.presentation.dto.response.OpenDataSearcherResponse;
import com.dxjunkyard.opendata.platform.presentation.dto.response.SearchConditionResponse;
import com.dxjunkyard.opendata.platform.presentation.dto.response.SearchResultInfoResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class OpenDataSearcherFactory {

    private final OpenDataRepository openDataRepository;

    private final UrlBuilderDomainService urlBuilderDomainService;

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

                final List<String> additionalQueryList = Stream.of(
                    Stream.of(StringUtils
                        .split(StringUtils.defaultString(request.keyword()), StringUtils.SPACE)).toList(),
                    organizationQueries,
                    categoryQueries
                ).flatMap(List::stream).toList();

                return SearchCondition.builder()
                    .keyword(CollectionUtils.isNotEmpty(additionalQueryList)
                        ? String.join(StringUtils.SPACE, additionalQueryList)
                        : null)
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
            return OpenDataSearcherResponse.builder()
                .searchResultInfo(new SearchResultInfoResponse(tokyoOpenData.getTotal()))
                .dataset(tokyoOpenData.getDataset())
                .searchCondition(SearchConditionResponse.from(searchCondition))
                .showMoreUrl(urlBuilderDomainService.build(searchCondition))
                .build();
        }

        return OpenDataSearcherResponse.empty();
    }
}
