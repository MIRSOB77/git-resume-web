package com.exozet.gitresumeweb.service;

import com.exozet.gitresumeweb.GitHubProperties;
import com.exozet.gitresumeweb.exception.InternalException;
import com.exozet.gitresumeweb.git.GetUserRepositoriesEntry;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.kohsuke.github.GHContentSearchBuilder;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(name = "app.github.jcabi.enabled", havingValue = "true")
@Service
public class GitHubJCABIService {

    @Autowired
    GitHubProperties gitHubProperties;

    GitHub gitHub;

    @PostConstruct
    public void init() throws IOException {
        GitHubProperties.GitClientProps jcabiProps = gitHubProperties.getJcabi();

        if (!(jcabiProps.getLogin().isEmpty() || jcabiProps.getOAuth().isEmpty())) {
            gitHub = GitHub.connect(jcabiProps.getLogin(), jcabiProps.getOAuth());
        } else {
            gitHub = GitHub.connectAnonymously();
        }
    }

    public GHUser getUser(String username) throws InternalException {
        GHUser ghUser = null;
        try {
            ghUser = gitHub.getUser(username);

            return ghUser;
        } catch (IOException e) {
            throw new InternalException(e);
        }
    }

    public Map<String, Integer> findEnvironments(GHUser user) {
        GHContentSearchBuilder searchContentBuilder = gitHub.searchContent().user(user.getLogin());

        Map environments = Maps.newHashMap();

        environments.put("Maven", gitHub.searchContent().user(user.getLogin()).filename("pom").list().asList().stream().map(e -> e.getOwner()).collect(Collectors.toSet()).size());
        environments.put("Gradle", gitHub.searchContent().user(user.getLogin()).extension("gradle").list().asList().stream().map(e -> e.getOwner()).collect(Collectors.toSet()).size());

        return environments;
    }

    public List<GetUserRepositoriesEntry> getRepositoriesByUsername(String username) {
        return null;
    }

    public List<GetUserRepositoriesEntry> getRepositoriesByUrl(String url) {
        return null;
    }
}
