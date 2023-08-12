package com.dxjunkyard.opendata.platform.domain.model.opendata;

import java.util.List;

public interface Dataset {

    List<DatasetFile> getDatasetFile();

    boolean isTooManyFiles();
}
