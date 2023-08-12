package com.dxjunkyard.opendata.platform.domain.model.search;

import com.dxjunkyard.opendata.platform.domain.model.opendata.CategoryId;
import com.dxjunkyard.opendata.platform.domain.model.opendata.OpenDataFormat;
import com.dxjunkyard.opendata.platform.domain.model.opendata.OrganizationId;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
@Builder
public class SearchCondition {

    @Nullable
    private final String keyword;

    @NonNull
    private final Prefecture prefecture;

    @NonNull
    private final Map<String, OrganizationId> organizationMap;

    @NonNull
    private final Map<String, CategoryId> categoryMap;

    @NonNull
    private final Set<OpenDataFormat> formatSet;

    @NonNull
    public Set<OrganizationId> getOrganizationIdSet() {
        return Set.copyOf(organizationMap.values());
    }

    @NonNull
    public Set<CategoryId> getCategoryIdSet() {
        return Set.copyOf(categoryMap.values());
    }

    @Nullable
    public String getOrganization() {

        if (organizationMap.isEmpty()) {
            return null;
        }

        return String.join(" ", organizationMap.keySet());
    }

    @Nullable
    public String getCategory() {

        if (categoryMap.isEmpty()) {
            return null;
        }

        return String.join(" ", categoryMap.keySet());
    }

    @Nullable
    public String getFormat() {

        if (formatSet.isEmpty()) {
            return null;
        }

        return String.join(" ", formatSet.stream()
            .map(OpenDataFormat::name)
            .collect(Collectors.toUnmodifiableSet()));
    }
}
