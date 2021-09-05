package projectBlog.customBlog.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import projectBlog.customBlog.dto.ArticleForm;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ArticleForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ArticleForm articleForm = (ArticleForm) target;
        if(!StringUtils.hasText(articleForm.getTitle())) {
            errors.rejectValue("title", "required.articleForm.title");
        }

        if(!StringUtils.hasText(articleForm.getContent())) {
            errors.rejectValue("content", "required.articleForm.content");
        }

        if(articleForm.getCategoryId() == null) {
            errors.rejectValue("categoryId", "required.articleForm.category");
        }

    }
}
