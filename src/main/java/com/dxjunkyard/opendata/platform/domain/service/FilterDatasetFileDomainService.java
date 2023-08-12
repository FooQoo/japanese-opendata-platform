package com.dxjunkyard.opendata.platform.domain.service;

import com.dxjunkyard.opendata.platform.domain.model.opendata.DatasetFile;
import com.dxjunkyard.opendata.platform.domain.model.search.SearchCondition;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilterDatasetFileDomainService {

    @NonNull
    public List<DatasetFile> filter(final List<DatasetFile> datasetFiles, final SearchCondition searchCondition) {
        return datasetFiles.stream()
            // nullを最後尾に持っていく
            .sorted(Comparator
                .comparing(DatasetFile::getLastModifiedTimestamp, Comparator.nullsFirst(Comparator.naturalOrder())).reversed() // 最新のものが先にくるように逆順ソート
                .thenComparing(file -> !file.isMatched(searchCondition)) // isMatchedがtrueのものが先にくるようにソート
            )
            .limit(5)
            .collect(Collectors.toList());
    }
}
