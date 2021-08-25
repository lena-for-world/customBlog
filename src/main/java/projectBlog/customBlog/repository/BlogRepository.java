package projectBlog.customBlog.repository;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import projectBlog.customBlog.domain.Blog;

@Repository
@RequiredArgsConstructor
public class BlogRepository {

    private final EntityManager em;

    public Blog findBlog(int blogId) {
        return em.find(Blog.class, blogId);
    }

    public List<Blog> findAllBlogs() {
        return em.createQuery("select b from Blog b", Blog.class).getResultList();
    }

}
