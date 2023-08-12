package com.dxjunkyard.opendata.platform.domain.model.search;

import com.dxjunkyard.opendata.platform.domain.model.opendata.OrganizationId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationNameToIdConverter {

    private final Map<String, OrganizationId> map;

    @NonNull
    public static OrganizationNameToIdConverter init() {
        return new OrganizationNameToIdConverter(new HashMap<>());
    }

    public void add(final String name, final String id) {
        map.put(name, OrganizationId.from(id));
    }

    @Nullable
    public OrganizationId convert(final String name) {
        return map.get(name);
    }

    public boolean contains(final String name) {
        return map.containsKey(name);
    }

    @NonNull
    public Set<String> getOrganizationNameList() {
        return map.keySet();
    }
}
