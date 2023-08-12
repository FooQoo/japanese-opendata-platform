package com.dxjunkyard.opendata.platform.presentation.dto.response;

public record SearchResultInfoResponse(
    Integer totalOfHits
) {
    public static SearchResultInfoResponse empty() {
        return new SearchResultInfoResponse(0);
    }
}
