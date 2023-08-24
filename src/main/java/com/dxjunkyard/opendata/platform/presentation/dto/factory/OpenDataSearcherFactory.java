package com.dxjunkyard.opendata.platform.presentation.dto.factory;

import com.dxjunkyard.opendata.platform.domain.model.opendata.Dataset;
import com.dxjunkyard.opendata.platform.domain.model.opendata.DatasetFile;
import com.dxjunkyard.opendata.platform.domain.model.opendata.OpenData;
import com.dxjunkyard.opendata.platform.domain.model.search.CategoryNameToIdConverter;
import com.dxjunkyard.opendata.platform.domain.model.search.OrganizationNameToIdConverter;
import com.dxjunkyard.opendata.platform.domain.model.search.Prefecture;
import com.dxjunkyard.opendata.platform.domain.model.search.condition.CategorySearchCondition;
import com.dxjunkyard.opendata.platform.domain.model.search.condition.KeywordSearchCondition;
import com.dxjunkyard.opendata.platform.domain.model.search.condition.OrganizationSearchCondition;
import com.dxjunkyard.opendata.platform.domain.model.search.condition.SearchCondition;
import com.dxjunkyard.opendata.platform.domain.service.FilterDatasetFileDomainService;
import com.dxjunkyard.opendata.platform.domain.service.RomajiConverterDomainService;
import com.dxjunkyard.opendata.platform.domain.service.UrlBuilderDomainService;
import com.dxjunkyard.opendata.platform.presentation.dto.request.OpenDataSearcherRequest;
import com.dxjunkyard.opendata.platform.presentation.dto.response.*;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OpenDataSearcherFactory {

    private final UrlBuilderDomainService urlBuilderDomainService;

    private final FilterDatasetFileDomainService filterDatasetFileDomainService;

    private final OrganizationNameToIdConverter organizationNameToIdConverter;

    private final CategoryNameToIdConverter categoryNameToIdConverter;

    private final RomajiConverterDomainService romajiConverterDomainService;

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
                    .language(request.language())
                    .build();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new ValidationException(illegalArgumentException.getMessage());
        }
    }

    @NonNull
    public OpenDataSearcherResponse build(final OpenData openData, final SearchCondition searchCondition) {
        final List<DatasetResponse> datasetResponse = openData.getDataset().stream()
            .map(dataset -> {
                final var datasetFileResponse = filterDatasetFileDomainService.filter(dataset.getFiles(), searchCondition)
                    .stream()
                    .map(datasetFile -> build(datasetFile, searchCondition))
                    .toList();

                return build(dataset, datasetFileResponse, searchCondition);
            }).toList();

        return OpenDataSearcherResponse.builder()
            .searchResultInfo(new SearchResultInfoResponse(openData.getTotal()))
            .dataset(datasetResponse)
            .searchCondition(SearchConditionResponse.from(searchCondition))
            .showMoreUrl(urlBuilderDomainService.build(searchCondition))
            .build();
    }

    public DatasetResponse build(final Dataset dataset, @Nullable final List<DatasetFileResponse> overriddenFiles, final SearchCondition searchCondition) {
        // 上書き用のオープンデータがあればそれを使う
        final var filesResponse = Objects.nonNull(overriddenFiles)
            ? overriddenFiles
            : dataset.getFiles().stream().map(DatasetFileResponse::from).toList();

        return DatasetResponse.builder()
            .title(searchCondition.isJapanese() ? dataset.getTitle() : romajiConverterDomainService.convert(dataset.getTitle()))
            .description(dataset.getDescription())
            .datasetUrl(dataset.getDatasetUrl())
            .maintainer(searchCondition.isJapanese() ? dataset.getMaintainer() : romajiConverterDomainService.convert(dataset.getMaintainer()))
            .license(searchCondition.isJapanese() ? dataset.getLicense() : romajiConverterDomainService.convert(dataset.getLicense()))
            .files(filesResponse)
            .build();
    }

    public DatasetFileResponse build(final DatasetFile datasetFile, final SearchCondition searchCondition) {
        return DatasetFileResponse.builder()
            .title(searchCondition.isJapanese() ? datasetFile.getTitle() : romajiConverterDomainService.convert(datasetFile.getTitle()))
            .description(datasetFile.getDescription())
            .format(datasetFile.getFormat())
            .url(datasetFile.getUrl())
            .build();
    }
}
