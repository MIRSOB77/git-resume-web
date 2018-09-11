package com.exozet.gitresumeweb.service;

import com.exozet.gitresumeweb.TestUtils;
import com.exozet.gitresumeweb.domain.GitUserResponse;
import com.exozet.gitresumeweb.exception.InternalException;
import com.exozet.gitresumeweb.exception.UserNotFoundException;
import com.google.gson.Gson;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GithubApiServiceTest {

    @Autowired
    GithubApiService githubApiService;

    @MockBean
    GitHubClient gitHubClient;

    @Test
    public void getUserProfile_Success() throws UserNotFoundException, IOException, InternalException {
         Mockito.when(gitHubClient.getUser(anyString())).
                 thenReturn(new Gson().fromJson(TestUtils.getResource("gitmocks/get_user.json"), GitUserResponse.class));

         githubApiService.getUserProfile("testuser");

    }

    @Test(expected = UserNotFoundException.class)
    public void getUserProfile_Failed() throws UserNotFoundException, IOException, InternalException {
        Mockito.when(gitHubClient.getUser(anyString())).thenThrow(UserNotFoundException.class);
        
        githubApiService.getUserProfile("testuser");
    }
    
}