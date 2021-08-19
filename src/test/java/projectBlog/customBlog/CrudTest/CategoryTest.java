package projectBlog.customBlog.CrudTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.domain.Blog;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Status;

@SpringBootTest
@Transactional
public class CategoryTest {

    @Autowired EntityManager em;
    Category category;
    String name;
    Blog blog;

    @BeforeEach
    public void before() {
        //given
        name = "카테고리1";
        blog = new Blog();
        save(blog);
        category = Category.makeParentCategory(name, blog);
    }

    public void save(Object object) {
        em.persist(object);
    }

    public void delete(Object object) {
        em.remove(object);
    }

    @Test
    @DisplayName("카테고리 저장 및 조회 - jpa")
    public void addCategory() {

        // when
        save(category);
        Category result = em.find(category.getClass(), category.getId());

        // then
        assertEquals(result.getName(), name);
        assertEquals(result.getStatus(), Status.Parent);
    }

    @Test
    @DisplayName("카테고리 삭제")
    public void removeCategory() {

        save(category);
        List<Category> categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 1);

        delete(categories.get(0));

        // then
        categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 0);
    }

    @Test
    @DisplayName("카테고리 전부 조회")
    public void getAllCategories() {
        String name2 = "카테고리2";
        Category category2 = Category.makeParentCategory(name2, blog);
        save(category);
        save(category2);
        List<Category> categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 2);

        for (Category cate: categories) {
            System.out.println("category = " + cate.getName());
        }

    }

    @Test
    @DisplayName("카테고리 수정")
    public void updateCategory() {

        // when
        save(category);
        List<Category> categories = em.createQuery("select c from Category c", Category.class).getResultList();
        assertEquals(categories.size(), 1);
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
        save(category);
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
        save(category);
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
        save(category);
        Category newChild = Category.makeChildCategory("newChild");
        try {
            category.addChildCategory(newChild);
        } catch(Exception e) {
            e.printStackTrace();
        }
        assertEquals(category.getChilds().get(0), newChild);
        assertEquals(category.getChilds().get(0).getName(), newChild.getName());
    }

}
