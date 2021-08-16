package projectBlog.customBlog.MainPageTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.Domain.Article;
import projectBlog.customBlog.Domain.Blog;
import projectBlog.customBlog.Domain.Category;
import projectBlog.customBlog.Domain.Member;

@SpringBootTest
@Transactional
public class BlogTest {

    @Autowired
    private EntityManager em;

    public void save(Object object) {
        em.persist(object);
    }

    public void delete(Object object) {
        em.remove(object);
    }


    @Test
    @DisplayName("메인 페이지에서 블로그 목록 출력")
    public void getAllBlogs() {

        Member member1 = Member.makeMember("park0602", "park");
        Member member2 = Member.makeMember("kimkimkim", "kim");

        member1.getBlog().addMember(member1);
        member2.getBlog().addMember(member2);

        save(member1);
        save(member2);

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for(Member mm : members) {
            System.out.println(mm.getBlog().getTitle());
        }

        List<Blog> blogs = em.createQuery("select b from Blog b", Blog.class).getResultList();
        for(Blog mm : blogs) {
            System.out.println(mm.getTitle());
        }
    }

    @Test
    @DisplayName("블로그 클릭하면 해당 블로그의 카테고리, 글 등 불러오기")
    public void findCategoriesAndArticles() {
        Member member1 = Member.makeMember("park0602", "park");
        Member member2 = Member.makeMember("kimkimkim", "kim");

        member1.getBlog().addMember(member1);
        member2.getBlog().addMember(member2);

        save(member1);
        save(member2);

        Category cate = Category.makeParentCategory("category");
        Article article = Article.makeArticle("제목", "내용", LocalDateTime.now(), member1, cate);
        save(cate);
        save(article);

        // when

        Blog blog = em.find(Blog.class, member1.getBlog().getId());
        List<Category> categories = blog.getCategories();
        assertEquals(categories.size(), 1);

        // blog의 memberId로 해당 멤버의 글들을 조회
        List<Article> articles = em.createQuery("select a from Article a where a.member.id = :member_id", Article.class)
            .setParameter("member_id", blog.getMember().getId())
            .getResultList();
        assertEquals(articles.size(), 1);

    }


}