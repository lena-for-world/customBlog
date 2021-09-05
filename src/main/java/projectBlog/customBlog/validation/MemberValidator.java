package projectBlog.customBlog.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import projectBlog.customBlog.dto.MemberForm;

@Component
@Slf4j
public class MemberValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        MemberForm memberForm = (MemberForm) target;

        if(!StringUtils.hasText(memberForm.getUserId()) || memberForm.getUserId().length() < 4) {
            errors.rejectValue("userId", "length.userId");
        }

        if (!StringUtils.hasText(memberForm.getPassword()) || memberForm.getPassword().length() < 8) {
            errors.rejectValue("password", "length.password");
        }

        if(!StringUtils.hasText(memberForm.getName())) {
            errors.rejectValue("name", "required");
        }

    }
}
