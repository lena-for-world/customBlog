package projectBlog.customBlog.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Status;

@Slf4j
@Component
public class CategoryValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Category.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Category category = (Category) target;

        /** 자식 카테고리가 있는 카테고리일 경우 삭제 불가 */
        if(category.getStatus() == Status.Parent && category.getChilds().size() > 0) {
            errors.reject("hasChild");
        }

        /** 글을 1개 이상 가지고 있는 카테고리일 경우 삭제 불가 */
        if(category.getArticles().size() > 0) {
            errors.reject("hasContents");
        }

        // 중복되는 이름 있는지 체크

    }
}
