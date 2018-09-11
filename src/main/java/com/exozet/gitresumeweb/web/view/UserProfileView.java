package com.exozet.gitresumeweb.web.view;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.ModelAttribute;


@Builder
public class UserProfileView extends AbstractBaseView {
    private String username;
    private String usersWebsite;
    private List<RepositoryView> repos;
    private RatioView languageRatios;
    
    @ModelAttribute("username")
    public String getUsername() {
        return username;
    }

    @ModelAttribute("website")
    public String getUsersWebsite() {
        return usersWebsite;
    }

    @ModelAttribute("repos")
    public List<RepositoryView> getRepos() {
        return repos;
    }
    
    @ModelAttribute("languages")
    public Set<RatioView.RatioViewEntry> getLanguages() {
        return languageRatios.getEntries();
    }




    @AllArgsConstructor
    @Data
    public static class RepositoryView{
        private String name;
        private String url;
        private String description;

    }

}

