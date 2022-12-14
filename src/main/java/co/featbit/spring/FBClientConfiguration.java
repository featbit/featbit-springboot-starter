package co.featbit.spring;

import co.featbit.server.FBClientImp;
import co.featbit.server.FBConfig;
import co.featbit.server.Factory;
import co.featbit.server.Utils;
import co.featbit.server.exterior.FBClient;
import co.featbit.server.exterior.HttpConfigurationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static com.google.common.base.Preconditions.checkArgument;

@Configuration
@EnableConfigurationProperties({FBClientConfigProperties.class})
public class FBClientConfiguration {

    @Bean
    public FBClient client(FBClientConfigProperties properties) {
        if (!properties.isOffline()) {
            checkArgument(Utils.isValidEnvSecret(properties.getEnvSecret()), "envSecret is invalid");
            checkArgument(Utils.isUrl(properties.getStreamingUrl()) || Utils.isUrl(properties.getEventUrl()), "streaming or event url is invalid");
        }
        HttpConfigurationBuilder httpConfigFactory = Factory.httpConfigFactory();
        if (properties.getProxyHost() != null && properties.getProxyPort() > 0) {
            httpConfigFactory.httpProxy(properties.getProxyHost(), properties.getProxyPort());
            if (properties.getProxyUser() != null && properties.getProxyPassword() != null) {
                httpConfigFactory.passwordAuthenticator(properties.getProxyUser(), properties.getProxyPassword());
            }
        }

        FBConfig.Builder builder = new FBConfig.Builder()
                .offline(properties.isOffline())
                .startWaitTime(Duration.ofSeconds(properties.getStartWait()))
                .streamingURL(properties.getStreamingUrl())
                .eventURL(properties.getEventUrl())
                .httpConfigFactory(httpConfigFactory);

        return new FBClientImp(properties.getEnvSecret(), builder.build());
    }
}
