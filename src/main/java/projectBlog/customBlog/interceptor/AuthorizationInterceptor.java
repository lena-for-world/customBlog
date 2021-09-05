package projectBlog.customBlog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import projectBlog.customBlog.SessionConst;
import projectBlog.customBlog.domain.Member;

@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {

        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);
        ModelMap model = modelAndView.getModelMap();

        Integer blogId = (Integer) model.getAttribute("blogId");

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if(blogId != member.getBlog().getId()) {
            log.info("블로그 주인이 아닌 로그인 사용자의 요청");
            response.sendRedirect("/?redirectURL=" + requestURI); /** */
        }
    }
}
