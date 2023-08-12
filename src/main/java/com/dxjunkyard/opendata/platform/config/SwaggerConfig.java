package com.dxjunkyard.opendata.platform.config;

import com.dxjunkyard.opendata.platform.domain.model.opendata.OpenDataFormat;
import com.dxjunkyard.opendata.platform.domain.model.search.CategoryNameToIdConverter;
import com.dxjunkyard.opendata.platform.domain.model.search.OrganizationNameToIdConverter;
import com.dxjunkyard.opendata.platform.domain.repository.opendata.OpenDataRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Profile("doc")
@Validated
@ConfigurationProperties(prefix = "springdoc.extension")
@RequiredArgsConstructor
public class SwaggerConfig {

    @NotNull
    private final String title;

    @NotNull
    private final String version;

    @NotNull
    private final String description;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")))
            .info(new Info()
                .title(title)
                .version(version)
                .description(description));
    }

    @Bean
    @NonNull
    public OperationCustomizer operationCustomizer(final OpenDataRepository openDataRepository) {

        // 事前に非同期で取得しておく
        final Mono<OrganizationNameToIdConverter> organizationNameToIdConverter = openDataRepository.fetchOrganization();
        final Mono<CategoryNameToIdConverter> categoryNameToIdConverter = openDataRepository.fetchCategory();

        final Set<String> organizationNameList = Optional.ofNullable(
                organizationNameToIdConverter
                    .map(OrganizationNameToIdConverter::getOrganizationNameList)
                    .block())
            .orElse(Set.of());

        final Set<String> categoryNameList = Optional.ofNullable(
                categoryNameToIdConverter
                    .map(CategoryNameToIdConverter::getCategoryNameList)
                    .block())
            .orElse(Set.of());

        return (operation, handlerMethod) -> {

            if ("searchOpenData".equals(handlerMethod.getMethod().getName())) {
                operation.getParameters().forEach(parameter -> {
                    switch (parameter.getName()) {
                        case "organization" ->
                            parameter.setDescription(parameter.getDescription() + " Options of Organization are " + StringUtils.join(organizationNameList, ",") + ".");
                        case "category" ->
                            parameter.setDescription(parameter.getDescription() + " Options of Category are " + StringUtils.join(categoryNameList, ",") + ".");
                        case "format" ->
                            parameter.setDescription(parameter.getDescription() + " Option of Format are " + StringUtils.join(OpenDataFormat.getValues(), ",") + ".");
                    }
                });
            }

            return operation;
        };
    }
}
