package projectBlog.customBlog.crudTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Blog;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Member;

@SpringBootTest
@Transactional
public class ArticleCrudTest {

    @Autowired EntityManager em;
    Article article;
    String title, content;
    Member member;
    Category category;
    Blog blog;

    public void save(Object object) {
        em.persist(object);
    }

    public void delete(Object object) {
        em.remove(object);
    }

    @BeforeEach
    void beforeEach() {
        title = "black widow";
        content = "content";
        member = new Member();
        blog = Blog.makeBlog("asdf", member);
        category = Category.makeParentCategory("category", blog);
        article = Article.makeArticle(title, content, LocalDateTime.now(), member, category);
        save(blog);
        save(category);
        save(member);
        save(article);
    }

    @Test
    @DisplayName("게시글 저장 - 메모리")
    public void addMemoryArticle() {
        TempDb tempDb = new TempDb();
        // when
        tempDb.save(article);
        // then
        assertEquals(tempDb.list.size(), 1);
    }

    @Test
    @DisplayName("게시글 저장 및 단일 조회 - jpa")
    public void addJpaArticle() {

        // when
        Article result = em.find(Article.class, article.getId());

        // then
        assertEquals(result.getContent(), content);
    }

    @Test
    @DisplayName("게시글 삭제")
    public void removeArticle() {

        List<Article> articles = em.createQuery("select a from Article a", Article.class).getResultList();
        assertEquals(articles.size(), 1);

        delete(articles.get(0));

        // then
        articles = em.createQuery("select a from Article a", Article.class).getResultList();
        assertEquals(articles.size(), 0);
    }

    @Test
    @DisplayName("게시글 수정")
    public void updateArticle() {

        // when
        //em.persist(article);
        List<Article> articles = em.createQuery("select a from Article a", Article.class).getResultList();
        assertEquals(articles.size(), 1);
        assertEquals(articles.get(0).getContent(), content);

        String editTitle = "수정된 제목";
        String editContent = "수정된 내용";
        articles.get(0).editContent(editTitle, editContent); // 더티 체킹 이용한 글 수정

        Article tempArticle = em.find(Article.class, articles.get(0).getId());

        // then
        assertNotEquals(editTitle, title);
        assertEquals(editContent, tempArticle.getContent());

    }

    @Test
    @DisplayName("최신 순 게시글 5개씩 조회")
    public void findFiveArticles() {
        // when ( total 6 articles )
        Member member2 = Member.makeMember("asdf", "1234", "asdfasfd");
        for(int i = 2; i < 7; i++) {
            title = "" + i;
            content = "게시글 " + i;
            article = Article.makeArticle(title, content, LocalDateTime.now(), member, category);
            save(article);
        }

        List<Article> articles = em.createQuery("select a from Article a", Article.class).getResultList();
        assertEquals(articles.size(), 6);

        List<Article> fiveArticles = em.createQuery("select a from Article a" +
            " join fetch a.member m" +
            " join fetch a.category c" +
            " order by a.id desc", Article.class)
            .setFirstResult(0)
            .setMaxResults(5)
            .getResultList();

        for(Article art : fiveArticles) {
            System.out.println("art.getContent() = " + art.getContent());
        }

        assertEquals(fiveArticles.size(), 5);
    }

    @Test
    @DisplayName("게시글 카테고리 이동")
    public void moveArticle() {

        // given
        Article arti = em.find(Article.class, article.getId());
        Category newCategory = Category.makeParentCategory("newCate", blog);
        assertNotEquals(arti.getCategory().getName(), newCategory.getName());

        // when
        article.moveArticleCategory(newCategory);

        // then
        assertEquals(newCategory, article.getCategory());

    }

}