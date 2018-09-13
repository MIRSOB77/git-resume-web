package com.exozet.gitresumeweb.service;

import com.exozet.gitresumeweb.git.GetUserRepositoriesEntry;
import com.exozet.gitresumeweb.git.GetUserResponse;
import com.exozet.gitresumeweb.exception.UserNotFoundException;
import java.util.List;

public interface GitHubClient {
    GetUserResponse getUser(String username) throws UserNotFoundException;
    List<GetUserRepositoriesEntry> getRepositoriesByUsername(String username);
    List<GetUserRepositoriesEntry> getRepositoriesByUrl(String url);

}
