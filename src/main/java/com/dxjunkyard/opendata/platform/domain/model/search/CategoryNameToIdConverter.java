package com.dxjunkyard.opendata.platform.domain.model.search;

import com.dxjunkyard.opendata.platform.domain.model.opendata.CategoryId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryNameToIdConverter {

    @NonNull
    private final Map<String, CategoryId> map;

    @NonNull
    public static CategoryNameToIdConverter init() {
        return new CategoryNameToIdConverter(new HashMap<>());
    }

    public void add(final String name, final String id) {
        map.put(name, CategoryId.from(id));
    }

    @Nullable
    public CategoryId convert(final String name) {
        return map.get(name);
    }

    public boolean contains(final String name) {
        return map.containsKey(name);
    }

    @NonNull
    public Set<String> getCategoryNameList() {
        return map.keySet();
    }
}
