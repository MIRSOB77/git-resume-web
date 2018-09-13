package com.exozet.gitresumeweb;

import com.exozet.gitresumeweb.conf.GitHubClientType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    private GitClientProps jcabi;

    @Data
    public static class GitClientProps{
        private String login;
        private String oAuth;

    }

    public String getUrlString() {
        return host + ( port != null && port > 0 ? (":"+port) : "" );
    }
}
