package projectBlog.customBlog.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import projectBlog.customBlog.domain.Member;
import projectBlog.customBlog.dto.MemberForm;
import projectBlog.customBlog.repository.CrudRepository;
import projectBlog.customBlog.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final CrudRepository crudRepository;

    @GetMapping("/add")
    public String addMember(@ModelAttribute("member") MemberForm form) {
        return "member/addMemberForm";
    }

    @PostMapping("/add")
    public String addMemberPost(MemberForm form, HttpServletRequest request) {
        Member member = Member.makeMember(form.getUserId(), form.getPassword(), form.getName());
        crudRepository.save(member);
        return "redirect:/";
    }

}
