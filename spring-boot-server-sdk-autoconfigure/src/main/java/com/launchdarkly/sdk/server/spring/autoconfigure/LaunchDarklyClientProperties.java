package com.launchdarkly.sdk.server.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "launchdarkly.client")
public class LaunchDarklyClientProperties {

    @Getter @Setter private String applicationSdkKey;
    @Getter @Setter private String applicationId;
    @Getter @Setter private String applicationVersion;
    @Getter @Setter private EndpointsProperties endpoints;
    @Getter @Setter private EventProperties events;
    @Getter @Setter private HttpProperties http;
    @Getter @Setter private boolean sendDiagnostics = true;

    public static class HttpProperties {
        @Getter @Setter private Long connectTimeoutSeconds;
        @Getter @Setter private Long socketTimeoutSeconds;
        @Getter @Setter private String proxyHost;
        @Getter @Setter private Integer proxyPort;
        @Getter @Setter private String proxyUser;
        @Getter @Setter private String proxyPassword;
    }

    public static class EventProperties {
        @Getter @Setter private boolean enabled = true;
    }

    public static class EndpointsProperties {
        @Getter @Setter private String eventsEndpointUri;
        @Getter @Setter private String pollingEndpointUri;
        @Getter @Setter private String relayProxyEndpointUri;
        @Getter @Setter private String streamingEndpointUri;
    }

}
