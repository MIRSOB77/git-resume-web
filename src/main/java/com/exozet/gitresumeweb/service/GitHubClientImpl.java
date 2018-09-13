package com.exozet.gitresumeweb.service;

import com.exozet.gitresumeweb.GitHubProperties;
import com.exozet.gitresumeweb.git.GetUserRepositoriesEntry;
import com.exozet.gitresumeweb.git.GetUserResponse;
import com.exozet.gitresumeweb.exception.UserNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubClientImpl implements GitHubClient{

    @Autowired
    private GitHubProperties gitHubProperties;


    @Autowired
    RestTemplate restTemplate;

    // header use in every operation
    HttpHeaders defaultHeader;

    public static final String API_PATH_USERS = "/users/{user}";
    public static final String API_PATH_REPOS = "/users/{user}/repos";

    public GitHubClientImpl(){
        
        defaultHeader = new HttpHeaders();
        defaultHeader.set(org.apache.http.HttpHeaders.ACCEPT, "application/vnd.github.v3.full+json");
    }

    public GetUserResponse getUser(String username) throws UserNotFoundException {

        HttpEntity userRequest = new HttpEntity(null, defaultHeader);

        ResponseEntity<GetUserResponse> userResponse =
            restTemplate.exchange(gitHubProperties.getUrlString() + API_PATH_USERS, HttpMethod.GET,  userRequest, GetUserResponse.class, username);

        if(userResponse.getStatusCode() == HttpStatus.NOT_FOUND){
            throw new UserNotFoundException();
        }

        return userResponse.getBody();
    }

    public List<GetUserRepositoriesEntry> getRepositoriesByUsername(String username){
        ParameterizedTypeReference<List<GetUserRepositoriesEntry>> responseType =
                new ParameterizedTypeReference<List<GetUserRepositoriesEntry>>() {};
        HttpEntity userRequest = new HttpEntity(null, defaultHeader);

        ResponseEntity<List<GetUserRepositoriesEntry>> responseEntity =
                restTemplate.exchange( gitHubProperties.getUrlString() + API_PATH_REPOS, HttpMethod.GET,  userRequest, responseType, username);



        return responseEntity.getBody();
    }

    public List<GetUserRepositoriesEntry> getRepositoriesByUrl(String url){
        ParameterizedTypeReference<List<GetUserRepositoriesEntry>> responseType =
                new ParameterizedTypeReference<List<GetUserRepositoriesEntry>>() {};
        HttpEntity userRequest = new HttpEntity(null, defaultHeader);

        ResponseEntity<List<GetUserRepositoriesEntry>> responseEntity =
                restTemplate.exchange( url, HttpMethod.GET,  userRequest, responseType);



        return responseEntity.getBody();

    }

}


                                      