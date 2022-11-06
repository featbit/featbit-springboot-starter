package co.featbit.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "featbit.spring")
public class FBClientConfigProperties {
    @NotBlank
    private String envSecret;
    @Min(0)
    private int startWait = 15;
    @NotBlank
    private String streamingUrl;
    @NotBlank
    private String eventUrl;

    private String proxyHost;

    private int proxyPort = -1;

    private String proxyUser;

    private String proxyPassword;

    private boolean offline = false;


}
