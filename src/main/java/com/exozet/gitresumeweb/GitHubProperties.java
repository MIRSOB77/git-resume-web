package com.exozet.gitresumeweb;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.github")
@Data
public class GitHubProperties {
    @NotBlank
    private String host;

    private Integer port;

    public String getUrlString() {
        return host + ( port != null && port > 0 ? (":"+port) : "" );
    }
}
