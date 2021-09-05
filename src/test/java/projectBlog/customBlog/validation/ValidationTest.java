package projectBlog.customBlog.validation;


import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.BindingResult;
import projectBlog.customBlog.dto.MemberForm;

@SpringBootTest
public class ValidationTest {

    @MockBean
    private BindingResult bindingResult;
    private MemberValidator memberValidator = new MemberValidator();

    @Test
    @DisplayName("회원 가입 시 아이디/비밀번호/이름 체크")
    public void memberTest() {
        MemberForm form = new MemberForm();
        form.setUserId("us");
        form.setPassword("test34");
        form.setName("kim");

        memberValidator.validate(form, bindingResult);

        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult);
        }

        System.out.println("성공");
    }

}
