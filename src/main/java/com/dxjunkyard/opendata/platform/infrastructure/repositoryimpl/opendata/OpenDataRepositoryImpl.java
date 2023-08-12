package com.dxjunkyard.opendata.platform.infrastructure.repositoryimpl.opendata;

import com.dxjunkyard.opendata.platform.domain.model.opendata.OpenData;
import com.dxjunkyard.opendata.platform.domain.model.search.CategoryNameToIdConverter;
import com.dxjunkyard.opendata.platform.domain.model.search.OrganizationNameToIdConverter;
import com.dxjunkyard.opendata.platform.domain.model.search.SearchCondition;
import com.dxjunkyard.opendata.platform.domain.repository.opendata.OpenDataRepository;
import com.dxjunkyard.opendata.platform.infrastructure.dto.factory.OpenDataSearchFactory;
import com.dxjunkyard.opendata.platform.infrastructure.dto.request.opendata.search.OpenDataSearchRequest;
import com.dxjunkyard.opendata.platform.infrastructure.dto.request.opendata.search.TokyoOpenDataSearchRequest;
import com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.area.TokyoOpenDataAreaResponse;
import com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.category.TokyoOpenDataCategoryResponse;
import com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.search.OpenDataSearchResponse;
import com.dxjunkyard.opendata.platform.infrastructure.repositoryimpl.opendata.tokyo.TokyoOpenDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OpenDataRepositoryImpl implements OpenDataRepository {

    // TODO: KeyValueにする
    private final TokyoOpenDataRepository tokyoOpenDataRepository;

    private final OpenDataSearchFactory openDataSearchFactory;

    @Override
    @NonNull
    public Mono<OpenData> search(final SearchCondition searchCondition) {

        final OpenDataSearchRequest request = openDataSearchFactory.build(searchCondition);

        final Mono<OpenDataSearchResponse> response;

        // キャストするために一度mapする
        if (request instanceof TokyoOpenDataSearchRequest tokyoRequest) {
            response = tokyoOpenDataRepository.search(tokyoRequest)
                .map(res -> res);
        } else {
            return Mono.error(new IllegalArgumentException("非対応のリクエスト条件です。" + request));
        }

        return response.map(openDataSearchFactory::build);
    }

    @Override
    @NonNull
    public Mono<OrganizationNameToIdConverter> fetchOrganization() {
        return tokyoOpenDataRepository.fetchArea().map(this::convertOrganization);
    }

    @Override
    @NonNull
    public Mono<CategoryNameToIdConverter> fetchCategory() {
        return tokyoOpenDataRepository.fetchCategory().map(this::convertCategory);
    }

    @NonNull
    private OrganizationNameToIdConverter convertOrganization(final TokyoOpenDataAreaResponse response) {
        final OrganizationNameToIdConverter converter = OrganizationNameToIdConverter.init();

        response.result().forEach(result -> converter.add(result.title(), result.name()));

        return converter;
    }

    @NonNull
    private CategoryNameToIdConverter convertCategory(final TokyoOpenDataCategoryResponse response) {
        final CategoryNameToIdConverter converter = CategoryNameToIdConverter.init();

        response.result().forEach(result -> converter.add(result.title(), result.name()));

        return converter;
    }
}
