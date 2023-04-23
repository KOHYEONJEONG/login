package hello.login.web.login;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {

    @NotEmpty //값 입력 안하면 안되니까!
    private String loginId;

    @NotEmpty
    private String password;
}
