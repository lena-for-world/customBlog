package projectBlog.customBlog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projectBlog.customBlog.SessionConst;
import projectBlog.customBlog.domain.Member;
import projectBlog.customBlog.dto.LoginForm;
import projectBlog.customBlog.service.LoginService;
import projectBlog.customBlog.validation.LoginValidator;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final LoginValidator loginValidator;

    @GetMapping("/login")
    public String getLoginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }


    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm form, @RequestParam(defaultValue="/") String redirectURL, HttpServletRequest request, BindingResult bindingResult) {

        loginValidator.validate(form, bindingResult);

        if(bindingResult.hasErrors()) {
            log.info("{}", bindingResult);
            return "login/loginForm";
        }

        Member member = loginService.login(form.getUserId(), form.getPassword());

        if(member == null) {
            return "login/loginForm";
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);
        Member m = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        redirectURL = "/blog/"+member.getBlog().getId();
        return "redirect:" + redirectURL;
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/";

    }
}
