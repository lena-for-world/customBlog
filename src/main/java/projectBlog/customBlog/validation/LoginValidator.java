package projectBlog.customBlog.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import projectBlog.customBlog.dto.LoginForm;
import projectBlog.customBlog.dto.MemberForm;

@Component
@Slf4j
public class LoginValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        LoginForm loginForm = (LoginForm) target;

        if(!StringUtils.hasText(loginForm.getUserId()) || loginForm.getUserId().length() < 4) {
            errors.rejectValue("userId", "length.userId");
        }

        if (!StringUtils.hasText(loginForm.getPassword()) || loginForm.getPassword().length() < 8) {
            errors.rejectValue("password", "length.password");
        }

    }
}
