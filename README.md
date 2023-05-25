# LaunchDarkly Spring Boot Server SDK

> This project is not officially supported by LaunchDarkly.

# Quickstart

To build and use the sdk, follow these steps.

1. Install [maven](https://maven.apache.com/):

```shell
brew install maven
```

2. Install the SDK to your local maven repository.

```shell
cd path/to/spring-boot-server-sdk
mvn install
```

3. Add the spring-boot-server-sdk-starter to your application's `pom.xml`:

```xml
<dependency>
    <groupId>com.launchdarkly</groupId>
    <artifactId>spring-boot-server-sdk-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

4. In your application's `application.properties`:

```properties
launchdarkly.client.application-id=<application name>
launchdarkly.client.application-version=<application version>
launchdarkly.client.application-sdk-key=<launchdarkly server sdk key>
```

5. In `@Component` classes:

```java
import com.launchdarkly.sdk.server.LDClient;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Component
public class MyServiceImpl {
    
    @Getter @Setter private LDClient ldClient;
    
    public void doWithFeatureFlag() {
        // use auto-injected LDClient
    }
    
}
```