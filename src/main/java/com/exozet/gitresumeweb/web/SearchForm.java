package com.exozet.gitresumeweb.web;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchForm {
    @NotBlank
    private String usernameTxt;

    //filter options
    private Boolean repositories = false;
    private Boolean languageRatios = false;

    private String saveBtn;

}
