package projectBlog.customBlog.repository;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Category;

@Repository
@Transactional
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

    // blog에 해당하는 모든 카테고리들을 찾아온다
    public List<Category> getAllCategoriesOfBlog(int blogId) {
        return em.createQuery("select c from Category c where c.blog.id = :blog_id")
            .setParameter("blog_id", blogId)
            .getResultList();
    }

    // 어떤 카테고리를 누르면 해당 카테고리에 해당하는 글들만 가져온다
    public List<Article> getArticlesOfCategory(int blogId, int cateId) {
        return em.createQuery("select a from Article a"
            + " where a.member.blog.id = :blog_id and a.category.id = :category_id")
            .setParameter("blog_id", blogId)
            .setParameter("category_id", cateId)
            .getResultList();
    }

    // 위와 동일한 기능에 페이징, 최신 순으로 정렬 추가
    public List<Article> getPageArticlesOfCategory(int blogId, int cateId, int startIndex, int pageSize) {
        return em.createQuery("select a from Article a"
            + " where a.member.blog.id = :blog_id and a.category.id = :category_id order by a.id desc")
            .setParameter("blog_id", blogId)
            .setParameter("category_id", cateId)
            .setFirstResult(startIndex)
            .setMaxResults(pageSize)
            .getResultList();
    }
}
