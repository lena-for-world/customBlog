package projectBlog.customBlog.CrudTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.Domain.Article;
import projectBlog.customBlog.Domain.Category;
import projectBlog.customBlog.Domain.Member;

@SpringBootTest
@Transactional
public class ArticleCrudTest {

    @Autowired EntityManager em;
    Article article;
    String title, content;
    Member member;
    Category category;

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
        category = Category.makeParentCategory("category");
        article = Article.makeArticle(title, content, LocalDateTime.now(), member, category);
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
    @DisplayName("게시글 5개씩 조회")
    public void findFiveArticles() {
        // when ( total 6 articles )
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
            " join fetch a.category c", Article.class)
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
        Category newCategory = Category.makeParentCategory("newCate");
        assertNotEquals(arti.getCategory().getName(), newCategory.getName());

        // when
        article.moveArticleCategory(newCategory);
        //Article findChangedArti = em.find(Article.class, article.getId());

        // then
        //assertNotEquals(arti.getCategory().getName(), newCategory.getName());
        // 업데이트된 article을 다시 땡겨오지 않아도 바뀐 정보가 반영되고 있는데,
        // article이 더티 체킹 되면
        // lazy loading이었던 엔티티의 멤버 엔티티에 접근하는 경우 다시 땡겨오는 건가?
        assertEquals(newCategory, article.getCategory());

    }

}