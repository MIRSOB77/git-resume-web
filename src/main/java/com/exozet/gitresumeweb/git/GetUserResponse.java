package com.exozet.gitresumeweb.git;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetUserResponse extends GitBaseResponse {
     @JsonProperty("html_url")
     private String websiteUrl;


     @JsonProperty("repos_url")
     private String reposUrl;

     @JsonProperty("login")
     private String username;
}
