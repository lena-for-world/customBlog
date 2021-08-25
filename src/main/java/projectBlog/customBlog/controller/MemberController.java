package projectBlog.customBlog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import projectBlog.customBlog.domain.Member;
import projectBlog.customBlog.dto.MemberForm;
import projectBlog.customBlog.service.CrudService;
import projectBlog.customBlog.validation.MemberValidator;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final CrudService crudService;
    private final MemberValidator memberValidator;

    @GetMapping("/add")
    public String addMember(@ModelAttribute("memberForm") MemberForm memberForm) {
        return "member/addMemberForm";
    }


    @PostMapping("/add")
    public String addMemberPost(@ModelAttribute MemberForm form, BindingResult bindingResult) {

        memberValidator.validate(form, bindingResult);

        if(bindingResult.hasErrors()) {
            log.info("{}", bindingResult);
            return "member/addMemberForm";
        }

        // 성공 로직
        Member member = Member.makeMember(form.getUserId(), form.getPassword(), form.getName());
        crudService.save(member);
        return "redirect:/";
    }

}
