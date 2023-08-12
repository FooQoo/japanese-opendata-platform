package com.dxjunkyard.opendata.platform.domain.service;

import com.dxjunkyard.opendata.platform.domain.model.search.SearchCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UrlBuilderDomainService {

    private static final String CATALOG_TOKYO_BASE_URL = "https://catalog.data.metro.tokyo.lg.jp/dataset";

    @NonNull
    public String build(final SearchCondition searchCondition) {

        final Map<String, Object> map = new HashMap<>();

        final var builder = UriComponentsBuilder.fromHttpUrl(CATALOG_TOKYO_BASE_URL);

        Optional.ofNullable(searchCondition.getKeyword())
            .filter(StringUtils::isNotBlank)
            .ifPresent(keyword -> {
                map.put("q", keyword);
                builder.queryParam("q", "{q}");
            });

        searchCondition.getOrganizationIdSet()
            .forEach(organizationId -> {
                map.put("organization_" + organizationId.getValue(), organizationId.getValue());
                builder.queryParam("organization", "{organization_" + organizationId.getValue() + "}");
            });

        searchCondition.getCategoryIdSet()
            .forEach(categoryId -> {
                map.put("groups_" + categoryId.getValue(), categoryId.getValue());
                builder.queryParam("groups", "{groups_" + categoryId.getValue() + "}");
            });

        searchCondition.getFormatSet()
            .forEach(format -> {
                map.put("res_format_" + format.getValue(), format.getValue());
                builder.queryParam("res_format", "{res_format_" + format.getValue() + "}");
            });

        return builder.build(map).toString();
    }
}
