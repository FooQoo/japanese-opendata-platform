package com.dxjunkyard.opendata.platform.presentation.controller;

import com.dxjunkyard.opendata.platform.application.service.SearchOpenDataService;
import com.dxjunkyard.opendata.platform.presentation.dto.factory.OpenDataSearcherFactory;
import com.dxjunkyard.opendata.platform.presentation.dto.request.OpenDataSearcherRequest;
import com.dxjunkyard.opendata.platform.presentation.dto.response.OpenDataSearcherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/opendata/v1/")
@RequiredArgsConstructor
public class OpenDataSearcherController {

    private final SearchOpenDataService searchOpenDataService;

    private final OpenDataSearcherFactory openDataSearcherFactory;

    @Operation(
        description = "Search OpenData.",
        tags = {"opendata"}
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = OpenDataSearcherResponse.class)
            )
        ),
    })
    @GetMapping("/search")
    public Mono<OpenDataSearcherResponse> searchOpenData(@ParameterObject @Validated final OpenDataSearcherRequest request) {
        return openDataSearcherFactory.build(request)
            .flatMap(searchCondition -> searchOpenDataService
                .search(searchCondition)
                .map(openData -> openDataSearcherFactory.build(openData, searchCondition)));
    }
}
