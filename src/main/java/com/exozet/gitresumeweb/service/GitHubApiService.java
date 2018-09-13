package com.exozet.gitresumeweb.service;

import com.exozet.gitresumeweb.git.GetUserRepositoriesEntry;
import com.exozet.gitresumeweb.git.GetUserResponse;
import com.exozet.gitresumeweb.exception.InternalException;
import com.exozet.gitresumeweb.exception.UserNotFoundException;
import com.exozet.gitresumeweb.web.view.RatioView;
import com.exozet.gitresumeweb.web.repos.RepositoryView;
import com.exozet.gitresumeweb.web.users.UserProfileView;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubApiService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    GitHubClientImpl gitHubClient;



    public UserProfileView getUserProfile(@Nonnull String username, boolean withRepos, boolean withLanguages) throws UserNotFoundException, InternalException {
        GetUserResponse getUserResponse = gitHubClient.getUser(username);

        List<GetUserRepositoriesEntry> repoResponseList = null;


        if(!StringUtils.isBlank(getUserResponse.getReposUrl())){
            repoResponseList = loadReposByUrl(getUserResponse.getReposUrl());
        } else {
            repoResponseList = loadReposByUser(username);
        }

        UserProfileView userProfileView =
                UserProfileView.builder().username(getUserResponse.getUsername()).usersWebsite(getUserResponse.getWebsiteUrl())
                        .repos( withRepos ? repoResponseList.stream().map(e -> toView(e)).collect(Collectors.toList()) : null)
                        .languageRatios( withLanguages ? reduceLanguages(repoResponseList) : null).build();
        return userProfileView;
    }


    public UserProfileView getUserProfile(@Nonnull String username) throws UserNotFoundException, InternalException {
        return getUserProfile(username, true, true);
    }


    private RepositoryView toView(GetUserRepositoriesEntry repo){
        return new RepositoryView(repo.getName(), repo.getUrl(), repo.getDescription());
    }

    public List<RepositoryView> getUserRepos(@Nonnull String username) {
        List<GetUserRepositoriesEntry> userRepos = loadReposByUser(username);

        return userRepos.stream()
                .map(e -> toView(e))
                .collect(Collectors.toList());
    }

    private List<GetUserRepositoriesEntry> loadReposByUser(@Nonnull String username) {
        List<GetUserRepositoriesEntry> userRepos = gitHubClient.getRepositoriesByUsername(username);

        return userRepos;
    }


    private List<GetUserRepositoriesEntry> loadReposByUrl(@Nonnull String url) {
        List<GetUserRepositoriesEntry> userRepos = gitHubClient.getRepositoriesByUrl(url);

        return userRepos;
    }

    private RatioView reduceLanguages(List<GetUserRepositoriesEntry> repositoryDataList) throws InternalException {

        RatioView languageRatios = null;
        try {
            languageRatios
                = new RatioView<GetUserRepositoriesEntry>
                    (repositoryDataList.stream()
                                     .filter(e -> e.getLanguage() != null)
                                     .collect(Collectors.toList()), GetUserRepositoriesEntry.class,"language");
        } catch (NoSuchMethodException e) {
            logger.error("language not member of class " + GetUserResponse.class.getName());

            throw new InternalException(e);
        }

        return languageRatios;
    }



}


