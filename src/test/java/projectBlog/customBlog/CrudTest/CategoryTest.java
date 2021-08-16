package projectBlog.customBlog.CrudTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.Domain.Category;
import projectBlog.customBlog.Domain.Status;

@SpringBootTest
@Transactional
public class CategoryTest {

    @Autowired EntityManager em;
    Category category;
    String name;

    @BeforeEach
    public void before() {
        //given
        name = "카테고리1";
        category = Category.makeParentCategory(name);
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
        Category category2 = Category.makeParentCategory(name2);
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

}
