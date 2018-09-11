package com.exozet.gitresumeweb.service;

import com.exozet.gitresumeweb.domain.GitRepositoryResponse;
import com.exozet.gitresumeweb.domain.GitUserResponse;
import com.exozet.gitresumeweb.exception.InternalException;
import com.exozet.gitresumeweb.exception.UserNotFoundException;
import com.exozet.gitresumeweb.web.view.RatioView;
import com.exozet.gitresumeweb.web.view.UserProfileView;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubApiService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    GitHubClient gitHubClient;

    public UserProfileView getUserProfile(@Nonnull String username) throws UserNotFoundException, InternalException {
        GitUserResponse gitUserResponse = gitHubClient.getUser(username);

        List<GitRepositoryResponse> repoResponseList = null;
        if(!StringUtils.isBlank(gitUserResponse.getReposUrl())){
            repoResponseList = loadReposByUrl(gitUserResponse.getReposUrl());
        } else {
            repoResponseList = loadReposByUser(username);
        }

        UserProfileView userProfileView =
                UserProfileView.builder().username(gitUserResponse.getUsername()).usersWebsite(gitUserResponse.getWebsiteUrl())
                        .repos( repoResponseList.stream().map(e -> toView(e)).collect(Collectors.toList()))
                        .languageRatios(reduceLanguages(repoResponseList)).build();
        return userProfileView;
    }

    private UserProfileView.RepositoryView toView(GitRepositoryResponse repo){
        return new UserProfileView.RepositoryView(repo.getName(), repo.getUrl(), repo.getDescription());
    }

    public List<UserProfileView.RepositoryView> getUserRepos(@Nonnull String username) {
        List<GitRepositoryResponse> userRepos = loadReposByUser(username);

        return userRepos.stream()
                .map(e -> toView(e))
                .collect(Collectors.toList());
    }

    private List<GitRepositoryResponse> loadReposByUser(@Nonnull String username) {
        List<GitRepositoryResponse> userRepos = gitHubClient.getRepositoriesByUsername(username);

        return userRepos;
    }


    private List<GitRepositoryResponse> loadReposByUrl(@Nonnull String url) {
        List<GitRepositoryResponse> userRepos = gitHubClient.getRepositoriesByUrl(url);

        return userRepos;
    }

    private RatioView reduceLanguages(List<GitRepositoryResponse> repositoryDataList) throws InternalException {

        RatioView languageRatios = null;
        try {
            languageRatios
                = new RatioView<GitRepositoryResponse>
                    (repositoryDataList.stream()
                                     .filter(e -> e.getLanguage() != null)
                                     .collect(Collectors.toList()), GitRepositoryResponse.class,"language");
        } catch (NoSuchMethodException e) {
            logger.error("language not member of class " + GitUserResponse.class.getName());

            throw new InternalException(e);
        }

        return languageRatios;
    }



}


