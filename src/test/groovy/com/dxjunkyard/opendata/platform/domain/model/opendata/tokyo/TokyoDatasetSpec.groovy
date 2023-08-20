package com.dxjunkyard.opendata.platform.domain.model.opendata.tokyo

import com.dxjunkyard.opendata.platform.domain.model.opendata.DatasetFile
import spock.lang.Specification

class TokyoDatasetSpec extends Specification {
    def "GetDatasetFile"() {
        given:
        def dataset = TokyoDataset.builder()
            .title("title")
            .description("description")
            .datasetUrl("datasetUrl")
            .maintainer("maintainer")
            .license("license")
            .files([
                TokyoDatasetFile.builder()
                    .title("title")
                    .description("description")
                    .format("format")
                    .url("url")
                    .build()
            ])
            .build()

        expect:
        dataset.getFiles().get(0) instanceof DatasetFile
    }
}
