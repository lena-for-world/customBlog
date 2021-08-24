package projectBlog.customBlog.CrudTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

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
import projectBlog.customBlog.domain.Status;

@SpringBootTest
@Transactional
public class CategoryTest {

    @Autowired EntityManager em;
    Category category, category2, category3;
    String name;
    Blog blog, blog2;
    Member member1, member2;
    Article article, article2;

    @BeforeEach
    public void before() {
        //given
        name = "카테고리1";
//        blog = new Blog();
//        save(blog);
//        blog2 = new Blog();
//        save(blog2);
        member1 = Member.makeMember("kimkimkim","1234", "kim");
        save(member1);
        member2 = Member.makeMember("kimkimkim", "1234","kim");
        save(member2);
        blog = member1.getBlog();
        blog2 = member2.getBlog();
        category = Category.makeParentCategory(name, blog);
        save(category);
        category2 = Category.makeParentCategory("카테고리2", blog2);
        save(category2);
        category3 = Category.makeParentCategory("카테고리3", blog2);
        save(category3);
        article = Article.makeArticle("제목", "내용", LocalDateTime.now(), member1, category);
        article2 = Article.makeArticle("제목22", "내용22", LocalDateTime.now(), member1, category2);
        Article article3 = Article.makeArticle("제목3", "내용3", LocalDateTime.now(), member1, category);
        Article article4 = Article.makeArticle("제목4", "내용4", LocalDateTime.now(), member1, category2);
        Article article5 = Article.makeArticle("제목5", "내용5", LocalDateTime.now(), member2, category2);
        Article article6 = Article.makeArticle("제목6", "내용6", LocalDateTime.now(), member1, category2);
        save(article);
        save(article2);
        save(article3);
        save(article4);
        save(article5);
        save(article6);

    }

    public void save(Object object) {
        em.persist(object);
    }

    public void delete(Object object) {
        em.remove(object);
    }

    @Test
    @DisplayName("카테고리 별 글 조회")
    public void getArticlesOfEachCategories() {
        List<Category> categories = em.createQuery("select c from Category c", Category.class).getResultList();
        int cateId = categories.get(0).getId();
        System.out.println("categories = " + categories.get(0).getName());
        List<Article> articles = em.createQuery("select a from Article a where a.category.id = :cate_id")
            .setParameter("cate_id", cateId)
            .getResultList();
        assertEquals(articles.size(), 2);
        for(Article a : articles) {
            System.out.println("a = " + a.getContent());
        }
    }

    @Test
    @DisplayName("카테고리 저장 및 조회 - jpa")
    public void addCategory() {

        // when
        Category result = em.find(category.getClass(), category.getId());

        // then
        assertEquals(result.getName(), name);
        assertEquals(result.getStatus(), Status.Parent);
    }

    @Test
    @DisplayName("카테고리 삭제")
    public void removeCategory() {

        // 카테고리가 글을 가지고 있을 때는 삭제되지 못하게 하고, 없을 때는 삭제 가능!
        List<Category> categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 3);

        if(categories.get(0).getArticles().size() > 0) {
            System.out.println("카테고리에 글이 있어서 삭제할 수 없어요!");
        } else {
            delete(categories.get(0));
        }
        // 글을 가지고 있지 않은 카테고리
        delete(categories.get(2));
        // then
        categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 2);
    }

    @Test
    @DisplayName("카테고리 자식 삭제하면 부모 삭제ㄴㄴ")
    public void removeChildCategory() {

        Category newChild = Category.makeChildCategory("newChild");
        try {
            category.addChildCategory(newChild);
        } catch(Exception e) {
            e.printStackTrace();
        }
        List<Category> categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 4);

        category.deleteCategory(newChild);
        delete(categories.get(categories.size()-1));

        // then
        categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 3);
    }

    @Test
    @DisplayName("카테고리 부모 삭제하면 자식 삭제")
    public void removeParentCategory() {

        Category newChild = Category.makeChildCategory("newChild");
        try {
            category3.addChildCategory(newChild);
        } catch(Exception e) {
            e.printStackTrace();
        }
        List<Category> categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 4);

        delete(category3);

        // then
        categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 2);
    }

    @Test
    @DisplayName("카테고리 전부 조회")
    public void getAllCategories() {

        List<Category> categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 3);

        for (Category cate: categories) {
            System.out.println("category = " + cate.getName());
        }

    }

    @Test
    @DisplayName("카테고리 수정")
    public void updateCategory() {

        // when
        List<Category> categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 3);
        assertEquals(categories.get(0).getName(), name);

        String editCategoryName = "수정된 이름";
        categories.get(0).editContent(editCategoryName); // 더티 체킹 이용한 글 수정

        Category tempCategory = em.find(Category.class, categories.get(0).getId());

        // then
        assertNotEquals(editCategoryName, name);
        assertEquals(editCategoryName, tempCategory.getName());

    }

    @Test
    @DisplayName("부모 카테고리에 부모 카테고리 생성 불가")
    public void noParentforParent() {
        // category == 부모 카테고리
        Category newParent = Category.makeParentCategory("newParent", blog);
        try {
            category.addChildCategory(newParent);
            fail();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("자식 카테고리 아래에 부모 카테고리 생성 불가")
    public void childHasNoChild() {
        // category == 부모 카테고리
        // given
        Category newChild = Category.makeChildCategory("newChild");
        try {
            // when
            newChild.addChildCategory(category);
            fail();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("부모 카테고리 아래에 자식 카테고리 생성")
    public void parentCategoryHasChildCategory() {
        Category newChild = Category.makeChildCategory("newChild");
        try {
            category.addChildCategory(newChild);
        } catch(Exception e) {
            e.printStackTrace();
        }
        assertEquals(category.getChilds().get(0), newChild);
        assertEquals(category.getChilds().get(0).getName(), newChild.getName());
    }

//    Article article = Article.makeArticle("제목", "내용", LocalDateTime.now(), member1, category);
//    Article article2 = Article.makeArticle("제목22", "내용22", LocalDateTime.now(), member1, category2);
    @Test
    @DisplayName("게시글 카테고리 이동")
    public void moveArticleToAnotherCategory() {

        assertNotEquals(category.getId(), article2.getCategory().getId());

        Category moveCategory = em.find(Category.class, category.getId());

        article2.moveArticleCategory(moveCategory);

        assertEquals(category.getId(), article2.getCategory().getId());
    }

}
