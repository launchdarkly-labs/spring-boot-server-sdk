package com.launchdarkly.sdk.server.spring.autoconfigure;

import static com.launchdarkly.sdk.server.Components.*;

import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.LDConfig;
import com.launchdarkly.sdk.server.integrations.EventProcessorBuilder;
import com.launchdarkly.sdk.server.integrations.HttpConfigurationBuilder;
import com.launchdarkly.sdk.server.integrations.ServiceEndpointsBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import lombok.Getter;

import java.time.Duration;
import java.util.Objects;

@AutoConfiguration
@ConditionalOnClass(LDClient.class)
@EnableConfigurationProperties(LaunchDarklyClientProperties.class)
public class LaunchDarklyClientAutoConfiguration {

    @Getter private LaunchDarklyClientProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public LDClient ldClient(final LDConfig ldConfig) {
        final String sdkKey = getProperties().getApplicationSdkKey();
        return new LDClient(sdkKey, ldConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public LDConfig ldConfig() {
        final LDConfig.Builder configBuilder = new LDConfig.Builder();
        final String applicationId = getProperties().getApplicationId();
        final String applicationVersion = getProperties().getApplicationVersion();
        if (Objects.nonNull(applicationId) || Objects.nonNull(applicationVersion)) {
            configBuilder.applicationInfo(applicationInfo()
                    .applicationId(applicationId)
                    .applicationVersion(applicationVersion)
            );
        }
        if (Objects.nonNull(getProperties().getEndpoints())) {
            final LaunchDarklyClientProperties.EndpointsProperties endpoints = getProperties().getEndpoints();
            final ServiceEndpointsBuilder endpointsConfiguration = serviceEndpoints();
            if (Objects.nonNull(endpoints.getEventsEndpointUri())) {
                endpointsConfiguration.events(endpoints.getEventsEndpointUri());
            }
            if (Objects.nonNull(endpoints.getPollingEndpointUri())) {
                endpointsConfiguration.polling(endpoints.getPollingEndpointUri());
            }
            if (Objects.nonNull(endpoints.getStreamingEndpointUri())) {
                endpointsConfiguration.streaming(endpoints.getStreamingEndpointUri());
            }
            if (Objects.nonNull(endpoints.getRelayProxyEndpointUri())) {
                endpointsConfiguration.relayProxy(endpoints.getRelayProxyEndpointUri());
            }
            configBuilder.serviceEndpoints(endpointsConfiguration);
        }
        if (Objects.nonNull(getProperties().getEvents())) {
            final LaunchDarklyClientProperties.EventProperties events = getProperties().getEvents();
            if (events.isEnabled()) {
                final EventProcessorBuilder eventsConfiguration = sendEvents();
                configBuilder.events(eventsConfiguration);
            }
            else {
                configBuilder.events(noEvents());
            }
        }
        if (Objects.nonNull(getProperties().getHttp())) {
            final LaunchDarklyClientProperties.HttpProperties http = getProperties().getHttp();
            final HttpConfigurationBuilder httpConfiguration = httpConfiguration();
            if (Objects.nonNull(http.getConnectTimeoutSeconds())) {
                httpConfiguration.connectTimeout(Duration.ofSeconds(http.getConnectTimeoutSeconds()));
            }
            if (Objects.nonNull(http.getSocketTimeoutSeconds())) {
                httpConfiguration.socketTimeout(Duration.ofSeconds(http.getSocketTimeoutSeconds()));
            }
            if (Objects.nonNull(http.getProxyHost()) && Objects.nonNull(http.getProxyPort())) {
                httpConfiguration.proxyHostAndPort(http.getProxyHost(), http.getProxyPort());
                if (Objects.nonNull(http.getProxyUser()) && Objects.nonNull(http.getProxyPassword())) {
                    httpConfiguration.proxyAuth(httpBasicAuthentication(http.getProxyUser(), http.getProxyPassword()));
                }
            }
            configBuilder.http(httpConfiguration);
        }
        configBuilder.diagnosticOptOut(!getProperties().isSendDiagnostics());
        return configBuilder.build();
    }

    @Autowired
    private void setProperties(final LaunchDarklyClientProperties properties) {
        this.properties = properties;
    }

}
