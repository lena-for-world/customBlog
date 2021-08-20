package projectBlog.customBlog.repository;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import projectBlog.customBlog.domain.Blog;
import projectBlog.customBlog.domain.Category;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public Category findCategory(int cateId) {
        return em.find(Category.class, cateId);
    }

    // 멤버의 카테고리를 찾는 조건이 포함되어야함
    public List<Category> findAllCategory() {
        return em.createQuery("select c from Category c", Category.class).getResultList();
    }
}
