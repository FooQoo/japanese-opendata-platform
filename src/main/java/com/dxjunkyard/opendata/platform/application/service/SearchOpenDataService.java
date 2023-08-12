package com.dxjunkyard.opendata.platform.application.service;

import com.dxjunkyard.opendata.platform.domain.model.opendata.OpenData;
import com.dxjunkyard.opendata.platform.domain.model.search.SearchCondition;
import com.dxjunkyard.opendata.platform.domain.repository.opendata.OpenDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SearchOpenDataService {

    private final OpenDataRepository openDataRepository;

    public Mono<OpenData> search(final SearchCondition searchCondition) {
        return openDataRepository.search(searchCondition);
    }
}
