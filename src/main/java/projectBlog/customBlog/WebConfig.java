package projectBlog.customBlog;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import projectBlog.customBlog.interceptor.AuthorizationInterceptor;
import projectBlog.customBlog.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /** 비회원 댓글 등록 방지 */
        registry.addInterceptor(new LoginInterceptor())
            .order(1)
            .addPathPatterns("/comment/**");

        /** 본인이 아닌 로그인 사용자의 게시글 등록/수정/삭제 및 카테고리 등록/수정/삭제 방지 */
         registry.addInterceptor(new AuthorizationInterceptor())
            .order(2)
            .addPathPatterns("/blog/write/**", "/blog/delete/**", "/blog/update/**");
    }
}
