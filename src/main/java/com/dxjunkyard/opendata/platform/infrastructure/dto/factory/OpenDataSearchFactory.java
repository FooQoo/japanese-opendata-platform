package com.dxjunkyard.opendata.platform.infrastructure.dto.factory;

import com.dxjunkyard.opendata.platform.domain.model.opendata.OpenData;
import com.dxjunkyard.opendata.platform.domain.model.search.condition.SearchCondition;
import com.dxjunkyard.opendata.platform.infrastructure.dto.request.opendata.search.OpenDataSearchRequest;
import com.dxjunkyard.opendata.platform.infrastructure.dto.request.opendata.search.TokyoOpenDataSearchRequest;
import com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.search.OpenDataSearchResponse;
import com.dxjunkyard.opendata.platform.infrastructure.dto.response.opendata.search.TokyoOpenDataSearchResponse;
import org.springframework.stereotype.Component;

@Component
public class OpenDataSearchFactory {

    public OpenDataSearchRequest build(final SearchCondition searchCondition) {
        switch (searchCondition.getPrefecture()) {
            case TOKYO:
                return TokyoOpenDataSearchRequest.from(searchCondition);
            case OTHER:
            default:
                throw new IllegalArgumentException();
        }
    }

    public OpenData build(final OpenDataSearchResponse response) {
        if (response instanceof TokyoOpenDataSearchResponse tokyoResponse) {
            return tokyoResponse.toTokyoOpenData();
        }

        throw new IllegalArgumentException();
    }
}
