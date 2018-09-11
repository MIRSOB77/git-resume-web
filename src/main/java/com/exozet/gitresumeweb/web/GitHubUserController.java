package com.exozet.gitresumeweb.web;

import com.exozet.gitresumeweb.exception.InternalException;
import com.exozet.gitresumeweb.exception.UserNotFoundException;
import com.exozet.gitresumeweb.service.GithubApiService;
import com.exozet.gitresumeweb.web.view.UserProfileView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("/users")
public class GitHubUserController {
    @Autowired
    GithubApiService githubApiService;

    @GetMapping
    public String index(){
        return "index";
    }

    @PostMapping(value = "/profile", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView showUserProfile(SearchForm search) throws UserNotFoundException, InternalException {
        UserProfileView profileView = githubApiService.getUserProfile(search.getUsernameTxt());
        
        return new ModelAndView("git_profile", "data", profileView);
    }

}
