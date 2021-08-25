package projectBlog.customBlog.mainPageTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Blog;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Member;

@SpringBootTest
@Transactional
public class BlogTest {

    @Autowired
    private EntityManager em;
    Member member1, member2;
    Blog blog;

    public void save(Object object) {
        em.persist(object);
    }

    public void delete(Object object) {
        em.remove(object);
    }

    @BeforeEach
    public void setUp() {

        member1 = Member.makeMember("park0602","1234", "park");
        member2 = Member.makeMember("kimkimkim", "1234", "kim");
        blog = member1.getBlog();
        save(member1);
        save(member2);

    }


    @Test
    @DisplayName("메인 페이지에서 블로그 목록 출력")
    public void getAllBlogs() {

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for(Member mm : members) {
            System.out.println(mm.getBlog().getTitle());
        }

        List<Blog> blogs = em.createQuery("select b from Blog b", Blog.class).getResultList();
        for(Blog mm : blogs) {
            System.out.println(mm.getMember().getName());
        }
    }

    @Test
    @DisplayName("블로그 클릭하면 해당 블로그의 카테고리, 글 등 불러오기")
    public void findCategoriesAndArticles() {

        Category cate = Category.makeParentCategory("category", blog);
        save(cate);
        member2 = Member.makeMember("kimkimkim", "1234","kim");
        save(member2);
        Article article = Article.makeArticle("제목", "내용", LocalDateTime.now(), member1, cate);
        Article article2 = Article.makeArticle("제목22", "내용22", LocalDateTime.now(), member1, cate);
        Article article3 = Article.makeArticle("제목3", "내용3", LocalDateTime.now(), member1, cate);
        Article article4 = Article.makeArticle("제목4", "내용4", LocalDateTime.now(), member1, cate);
        Article article5 = Article.makeArticle("제목5", "내용5", LocalDateTime.now(), member1, cate);
        Article article6 = Article.makeArticle("제목6", "내용6", LocalDateTime.now(), member1, cate);
        Article article7 = Article.makeArticle("제목7", "내용7", LocalDateTime.now(), member2, cate);
        save(article);
        save(article2);
        save(article3);
        save(article4);
        save(article5);
        save(article6);
        save(article7);

        // when
        Blog blog = em.find(Blog.class, member1.getBlog().getId()); // 이 부분은 view에서 블로그를 눌렀을 때 controller단으로 블로그 pk를 전송받는 걸로 대체 해야 할 부분
        List<Category> categories = blog.getCategories();
        for(Category c : categories) {
            System.out.println("c.getName() = " + c.getName());
        }
        assertEquals(categories.size(), 2);

        List<Article> articles = em.createQuery("select a from Article a").getResultList();
        assertEquals(articles.size(), 7);

        // 이 코드는 페이징이 안 됨
//        List<Article> articles = em.createQuery("select a from Article a where a.member.id = :member_id order by a.id desc", Article.class)
//            .setParameter("member_id", blog.getMember().getId())
//            .getResultList();

        // blog의 memberId로 해당 멤버의 글들을 조회
        articles = em.createQuery("select a from Article a"
            +" join fetch a.member m" +
            " join fetch a.category c" +
            " where a.member.id = :member_id order by a.id desc", Article.class)
            .setParameter("member_id", blog.getMember().getId())
            .setFirstResult(0)
            .setMaxResults(5)
            .getResultList();
        assertEquals(articles.size(), 5);
        for(Article ar : articles) {
            System.out.println(ar.getContent());
        }

    }

    @Test
    @DisplayName("각 카테고리를 눌렀을 때 카테고리의 글 가져오기")
    public void getAllArticlesFromCategory() {
        // given
        List<Category> categories = blog.getCategories();
        Category cate = Category.makeParentCategory("category", blog);
        Category dflt = categories.get(0);
        save(cate);

        // when
        Article article = Article.makeArticle("제목", "내용", LocalDateTime.now(), member1, cate);
        Article article2 = Article.makeArticle("제목22", "내용22", LocalDateTime.now(), member1, cate);
        Article article3 = Article.makeArticle("제목22", "내용22", LocalDateTime.now(), member1, dflt);
        save(article);
        save(article2);
        save(article3);

        // then
        assertEquals(cate.getArticles().size(), 2);
        assertEquals(dflt.getArticles().size(), 1);
    }
}