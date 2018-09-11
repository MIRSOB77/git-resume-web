package com.exozet.gitresumeweb.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitUserResponse extends GitBaseResponse {
     @JsonProperty("html_url")
     private String websiteUrl;


     @JsonProperty("repos_url")
     private String reposUrl;

     @JsonProperty("login")
     private String username;

     
     /*
              "repos_url": "https://api.github.com/users/octocat/repos",
              "events_url": "https://api.github.com/users/octocat/events{/privacy}",
              "received_events_url": "https://api.github.com/users/octocat/received_events",
              "type": "User",
              "site_admin": false,
              "name": "monalisa octocat",
              "company": "GitHub",
              "blog": "https://github.com/blog",
              "location": "San Francisco",
              "email": "octocat@github.com",
              "hireable": false,
              "bio": "There once was...",
              "public_repos": 2,
              "public_gists": 1,
              "followers": 20,
              "following": 0,
              "created_at": "2008-01-14T04:33:35Z",
              "updated_at": "2008-01-14T04:33:35Z"
              */

}
