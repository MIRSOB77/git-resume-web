package com.exozet.gitresumeweb.web;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchForm {
    @NotBlank
    private String usernameTxt;

    private String saveBtn;

}
