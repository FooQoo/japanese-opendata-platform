package com.dxjunkyard.opendata.platform.infrastructure.repositoryimpl.opendata.tokyo;

import com.dxjunkyard.opendata.platform.infrastructure.repositoryimpl.config.WebClientConfig;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("doc")
@Validated
@ConfigurationProperties(prefix = "repository.open-data.tokyo")
@RequiredArgsConstructor
public class TokyoOpenDataRepositoryConfig {

    @NotNull
    private final String url;

    @NotNull
    @Getter
    private final String searchOpenDataPath;

    @NotNull
    @Getter
    private final String fetchAreaPath;

    @NotNull
    @Getter
    private final String fetchCategoryPath;

    @Bean
    public WebClient tokyoOpenDataClient(final WebClient.Builder builder) {
        return builder.baseUrl(url)
            .filter(WebClientConfig.logRequest())
            .filter(WebClientConfig.logResponse())
            .build();
    }
}
