package com.fangao.dev.sys.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginDTO{

    private String username;
    private String password;
    private String code;
    private Boolean codeFlag;

}
