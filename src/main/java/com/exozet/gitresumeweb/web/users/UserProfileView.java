package com.exozet.gitresumeweb.web.users;

import com.exozet.gitresumeweb.domain.Repository;
import com.exozet.gitresumeweb.web.repos.RepositoryView;
import com.exozet.gitresumeweb.web.view.AbstractBaseView;
import com.exozet.gitresumeweb.web.view.RatioView;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.springframework.web.bind.annotation.ModelAttribute;


@Builder
public class UserProfileView extends AbstractBaseView {
    private String username;
    private String usersWebsite;
    private List<RepositoryView> repos;
    private RatioView languageRatios;
    private List<BuildEnvView> buildEnvironments;
    private Instant avgProjectDuration;

    @ModelAttribute("username")
    public String getUsername() {
        return username;
    }

    @ModelAttribute("website")
    public String getUsersWebsite() {
        return usersWebsite;
    }

    @ModelAttribute("repoCount")
    public Integer getCountRepos() {
        return repos.size();
    }

    @ModelAttribute("repos")
    public List<RepositoryView> getRepos() {
        return repos;
    }
    
    @ModelAttribute("languages")
    public Set<RatioView.RatioViewEntry> getLanguages() {
        return languageRatios.getEntries();
    }

    @ModelAttribute("buildEnvironments")
    public List<BuildEnvView> getBuildEnvironments(){
        return buildEnvironments;
    }

    @ModelAttribute("avgProjectDuration")
    public String getAvgProjectDuration(){
        if(avgProjectDuration == null) return "undefined";

        Date duration = Date.from(avgProjectDuration);

        return duration.getMonth() + " months " + duration.getDay() + " days " + new SimpleDateFormat("hh:mm:ss").format(duration);
    }
    
    public static UserProfileView create(GHUser ghUser, Map<String, Integer> environments) throws IOException, NoSuchMethodException {
        Collection<GHRepository> repositories = ghUser.getRepositories().values();

        return UserProfileView.builder()
                                 .username(ghUser.getName())
                                 .usersWebsite(ghUser.getHtmlUrl().toString())
                                 .repos(repositories.stream()
                                         .map(e -> RepositoryView.create(e)).collect(Collectors.toList()))
                                 .languageRatios(new RatioView<GHRepository>(Lists.newArrayList(repositories), GHRepository.class,"language"))
                                 .avgProjectDuration(Repository.calculateProjectAverageDuration(Lists.newArrayList(repositories)))
                                 .buildEnvironments(environments.entrySet().stream().map(e -> new BuildEnvView(e.getKey(),e.getValue()))
                                                                            .collect(Collectors.toList()))
                              .build();
    }

    @Data
    @AllArgsConstructor
    public static class BuildEnvView{
        private String name;
        private Integer count;
    }
}

