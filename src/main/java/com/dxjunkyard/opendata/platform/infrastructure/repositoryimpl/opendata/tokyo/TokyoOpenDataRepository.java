package com.dxjunkyard.opendata.platform.infrastructure.repositoryimpl.opendata.tokyo;

import com.dxjunkyard.opendata.platform.infrastructure.dto.request.opendata.search.TokyoOpenDataSearchRequest;
import com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.organization.TokyoOpenDataOrganizationResponse;
import com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.category.TokyoOpenDataCategoryResponse;
import com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.search.TokyoOpenDataSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokyoOpenDataRepository {

    private final WebClient tokyoOpenDataClient;

    private final TokyoOpenDataRepositoryConfig tokyoOpenDataRepositoryConfig;

    @NonNull
    public Mono<TokyoOpenDataSearchResponse> search(final TokyoOpenDataSearchRequest request) {

        final Map<String, Object> searchRequestParameterMap = new HashMap<>();

        searchRequestParameterMap.put("rows", request.rows());
        searchRequestParameterMap.put("start", request.start());
        Optional.ofNullable(request.q())
            .ifPresent(q -> searchRequestParameterMap.put("q", q));

        return tokyoOpenDataClient.get()
            .uri(uriBuilder -> {
                uriBuilder.path(tokyoOpenDataRepositoryConfig.getSearchOpenDataPath());
                searchRequestParameterMap.keySet()
                    .forEach(value -> uriBuilder.queryParam(value, "{" + value + "}"));
                return uriBuilder.build(searchRequestParameterMap);
            })
            .retrieve()
            .bodyToMono(TokyoOpenDataSearchResponse.class);
    }

    @NonNull
//    @Cacheable("organization")
    public Mono<TokyoOpenDataOrganizationResponse> fetchArea() {

        final Map<String, Object> areaRequestParameterMap = Map.of(
            "all_fields", true
        );

        return tokyoOpenDataClient.get()
            .uri(uriBuilder -> {
                uriBuilder.path(tokyoOpenDataRepositoryConfig.getFetchAreaPath());
                areaRequestParameterMap.keySet()
                    .forEach(value -> uriBuilder.queryParam(value, "{" + value + "}"));
                return uriBuilder.build(areaRequestParameterMap);
            })
            .retrieve()
            .bodyToMono(TokyoOpenDataOrganizationResponse.class);
    }

    @NonNull
//    @Cacheable("category")
    public Mono<TokyoOpenDataCategoryResponse> fetchCategory() {

        final Map<String, Object> categoryRequestParameterMap = Map.of(
            "all_fields", true
        );

        return tokyoOpenDataClient.get()
            .uri(uriBuilder -> {
                uriBuilder.path(tokyoOpenDataRepositoryConfig.getFetchCategoryPath());
                categoryRequestParameterMap.keySet()
                    .forEach(value -> uriBuilder.queryParam(value, "{" + value + "}"));
                return uriBuilder.build(categoryRequestParameterMap);
            })
            .retrieve()
            .bodyToMono(TokyoOpenDataCategoryResponse.class);
    }
}
