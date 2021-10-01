package projectBlog.customBlog.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projectBlog.customBlog.SessionConst;
import projectBlog.customBlog.domain.Blog;
import projectBlog.customBlog.domain.Member;
import projectBlog.customBlog.repository.BlogRepository;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BlogRepository blogRepository;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);

        List<Blog> blogList = blogRepository.findAllBlogs();

        model.addAttribute("blogs", blogList);

        if(session == null) {
            return "cover";
        }

        Member member= (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(member == null) {
            return "cover";
        }
        model.addAttribute("member", member);
        return "bs_blog/index";
    }

}
