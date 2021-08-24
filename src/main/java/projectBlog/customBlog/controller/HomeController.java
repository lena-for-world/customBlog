package projectBlog.customBlog.controller;

import java.lang.ProcessBuilder.Redirect;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projectBlog.customBlog.SessionConst;
import projectBlog.customBlog.domain.Member;
import projectBlog.customBlog.dto.LoginForm;
import projectBlog.customBlog.service.LoginService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if(session == null) {
            return "home";
        }

        Member member= (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(member == null) {
            return "home";
        }
        model.addAttribute("member", member);
        return "loginHome";
    }

}
