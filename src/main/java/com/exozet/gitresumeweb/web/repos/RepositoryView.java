package com.exozet.gitresumeweb.web.repos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.kohsuke.github.GHRepository;

@AllArgsConstructor
@Data
public class RepositoryView {
    private String name;
    private String url;
    private String description;

    public static RepositoryView create(GHRepository ghRepository){
        return new RepositoryView(ghRepository.getName(), ghRepository.getUrl().toString(), ghRepository.getDescription());

    }

}
