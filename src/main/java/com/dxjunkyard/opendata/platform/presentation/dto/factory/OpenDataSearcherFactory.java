package com.dxjunkyard.opendata.platform.presentation.dto.factory;

import com.dxjunkyard.opendata.platform.domain.model.opendata.OpenData;
import com.dxjunkyard.opendata.platform.domain.model.opendata.tokyo.TokyoOpenData;
import com.dxjunkyard.opendata.platform.domain.model.search.CategoryNameToIdConverter;
import com.dxjunkyard.opendata.platform.domain.model.search.OrganizationNameToIdConverter;
import com.dxjunkyard.opendata.platform.domain.model.search.Prefecture;
import com.dxjunkyard.opendata.platform.domain.model.search.condition.CategorySearchCondition;
import com.dxjunkyard.opendata.platform.domain.model.search.condition.KeywordSearchCondition;
import com.dxjunkyard.opendata.platform.domain.model.search.condition.OrganizationSearchCondition;
import com.dxjunkyard.opendata.platform.domain.model.search.condition.SearchCondition;
import com.dxjunkyard.opendata.platform.domain.service.FilterDatasetFileDomainService;
import com.dxjunkyard.opendata.platform.domain.service.UrlBuilderDomainService;
import com.dxjunkyard.opendata.platform.presentation.dto.request.OpenDataSearcherRequest;
import com.dxjunkyard.opendata.platform.presentation.dto.response.*;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenDataSearcherFactory {

    private final UrlBuilderDomainService urlBuilderDomainService;

    private final FilterDatasetFileDomainService filterDatasetFileDomainService;

    private final OrganizationNameToIdConverter organizationNameToIdConverter;

    private final CategoryNameToIdConverter categoryNameToIdConverter;

    @NonNull
    public SearchCondition build(final OpenDataSearcherRequest request) {
        try {
            final OrganizationSearchCondition organizationSearchCondition = OrganizationSearchCondition.of(
                organizationNameToIdConverter,
                request.organizationSet());

            final CategorySearchCondition categorySearchCondition = CategorySearchCondition.of(
                categoryNameToIdConverter,
                request.categorySet());

            final KeywordSearchCondition keywordSearchCondition = KeywordSearchCondition.of(request.keyword());

            return SearchCondition.builder()
                    .page(request.page())
                    .keywordSearchCondition(keywordSearchCondition)
                    // 現時点は東京のみ対応
                    .prefecture(Prefecture.TOKYO)
                    .organizationSearchCondition(organizationSearchCondition)
                    .categorySearchCondition(categorySearchCondition)
                    .formatSet(request.formatSet())
                    .build();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new ValidationException(illegalArgumentException.getMessage());
        }
    }

    @NonNull
    public OpenDataSearcherResponse build(final OpenData openData, final SearchCondition searchCondition) {
        if (openData instanceof TokyoOpenData tokyoOpenData) {
            final List<DatasetResponse> datasetResponse = tokyoOpenData.getDataset().stream()
                .map(dataset -> {
                    final var datasetFileResponse = filterDatasetFileDomainService.filter(dataset.getDatasetFile(), searchCondition)
                        .stream()
                        .map(DatasetFileResponse::from)
                        .toList();

                    return DatasetResponse.from(dataset, datasetFileResponse);
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
