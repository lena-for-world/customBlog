package projectBlog.customBlog.crudTest;

import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.domain.Blog;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Member;

@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired private EntityManager em;

    @Test
    public void 회원_생성시_블로그_카테고리_생성() {

        Member member = Member.makeMember("test", "test1234", "KIM");
        em.persist(member);
        int id = member.getId();

        Member getMember = em.find(Member.class, id);
        Blog blog = em.find(Blog.class, member.getBlog().getId());
        Assertions.assertEquals(getMember.getBlog(), blog);

        Category category = em.find(Category.class, blog.getCategories().get(0).getId());
        Assertions.assertEquals(getMember.getBlog().getCategories().get(0), category);

    }
    
}
