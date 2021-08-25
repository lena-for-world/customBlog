package projectBlog.customBlog.crudTest;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
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
import projectBlog.customBlog.repository.CrudRepository;

@SpringBootTest
@Transactional
public class CategoryBlogArticleTest {

    @Autowired EntityManager em;
    @Autowired CrudRepository crudRepository;
    Category category11, category12, category21;
    String name;
    Blog blog, blog2;
    Member member1, member2;

    @BeforeEach
    public void before() {
        //give
        member1 = Member.makeMember("kimkimkim", "1234","kim");
        crudRepository.save(member1);
        member2 = Member.makeMember("wtf", "1234","ssikbbang");
        crudRepository.save(member2);
        blog = member1.getBlog();
        blog2 = member2.getBlog();
        category11 = Category.makeParentCategory("카테고리11", blog);
        crudRepository.save(category11);
        category12 = Category.makeParentCategory("카테고리12", blog);
        crudRepository.save(category12);
        category21 = Category.makeParentCategory("카테고리21", blog2);
        crudRepository.save(category21);
        Article article = Article.makeArticle("제목", "내용", LocalDateTime.now(), member1, category11);
        Article article2 = Article.makeArticle("제목2", "내용2", LocalDateTime.now(), member1, category12);
        Article article3 = Article.makeArticle("제목3", "내용3", LocalDateTime.now(), member1, category12);
        Article article4 = Article.makeArticle("제목4", "내용4", LocalDateTime.now(), member2, category21);
        Article article5 = Article.makeArticle("제목5", "내용5", LocalDateTime.now(), member2, category21);
        Article article6 = Article.makeArticle("제목6", "내용6", LocalDateTime.now(), member1, category12);
        crudRepository.save(article);
        crudRepository.save(article2);
        crudRepository.save(article3);
        crudRepository.save(article4);
        crudRepository.save(article5);
        crudRepository.save(article6);
    }

    @Test
    @DisplayName("해당 블로그의 해당 카테고리에 해당하는 글만 가져오기")
    public void getArticlesOfTheCategoryOfTheBlog() {
        // 블로그의 id, 카테고리 id로 글을 찾아오기
        int blogId = blog.getId();
        int cateId = category12.getId();
        int page = 1;
        List<Article> articles = em.createQuery("select a from Article a"
        + " where a.member.blog.id = :blog_id and a.category.id = :category_id")
            .setParameter("blog_id", blogId)
            .setParameter("category_id", cateId)
            .setFirstResult(page*2)
            .setMaxResults(2)
            .getResultList();
        Assertions.assertEquals(articles.size(), 1);
        for(Article a : articles) {
            System.out.println(a.getTitle());
        }
    }


}
