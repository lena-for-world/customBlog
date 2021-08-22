package projectBlog.customBlog.service;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectBlog.customBlog.domain.Blog;


@Service
@RequiredArgsConstructor
public class MainService {

    private final EntityManager em;

    public List<Blog> findAllBlogList() {
        return em.createQuery("select b from Blog b", Blog.class).getResultList();
    }
}
