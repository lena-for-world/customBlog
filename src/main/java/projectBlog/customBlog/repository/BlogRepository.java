package projectBlog.customBlog.repository;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Blog;
import projectBlog.customBlog.domain.Category;

@Repository
@RequiredArgsConstructor
public class BlogRepository {

    private final EntityManager em;

    public Blog findBlog(int blogId) {
        return em.find(Blog.class, blogId);
    }

    public List<Blog> findAllBlogList() {
        return em.createQuery("select b from Blog b", Blog.class).getResultList();
    }

    // blog에 해당하는 모든 카테고리들을 찾아온다
    public List<Category> getAllCategoriesOfBlog(int blogId) {
        return em.createQuery("select c from Category c where c.blog.id = :blog_id")
            .setParameter("blog_id", blogId)
            .getResultList();
    }


}
