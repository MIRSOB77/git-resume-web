package com.exozet.gitresumeweb.web.users;

import com.exozet.gitresumeweb.exception.InternalException;
import com.exozet.gitresumeweb.exception.UserNotFoundException;
import com.exozet.gitresumeweb.service.GitHubApiService;
import com.exozet.gitresumeweb.service.GitHubJCABIService;
import com.exozet.gitresumeweb.web.SearchForm;
import java.io.IOException;
import org.kohsuke.github.GHUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("/users")
public class GitHubUserController {
    @Autowired
    GitHubApiService gitHubApiService;

    @Autowired(required = false)
    GitHubJCABIService gitHubJCABIService;

    @GetMapping
    public String index(){
        return "index";
    }

    @PostMapping(value = "/profile", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView showUserProfile(SearchForm search) throws UserNotFoundException, InternalException, NoSuchMethodException {
        UserProfileView profileView = null;

        if(gitHubJCABIService != null){
            GHUser ghUser = gitHubJCABIService.getUser(search.getUsernameTxt());
            try {
                profileView = UserProfileView.create(ghUser, gitHubJCABIService.findEnvironments(ghUser));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            profileView
                =   gitHubApiService.getUserProfile(search.getUsernameTxt(), search.getRepositories(),search.getLanguageRatios());
        }

        
        return new ModelAndView("git_profile", "data", profileView);
    }

}
