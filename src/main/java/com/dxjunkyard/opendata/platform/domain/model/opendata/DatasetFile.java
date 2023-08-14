package com.dxjunkyard.opendata.platform.domain.model.opendata;

import com.dxjunkyard.opendata.platform.domain.model.search.condition.SearchCondition;

public interface DatasetFile {

    String getTitle();

    String getDescription();

    String getFormat();

    String getUrl();

    Long getLastModifiedTimestamp();

    boolean isMatched(SearchCondition searchCondition);
}
