package projectBlog.customBlog.service;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Blog;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.repository.BlogRepository;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final EntityManager em;
    private BlogRepository blogRepository;

    // blog에 해당하는 모든 카테고리들을 찾아온다
    public List<Category> getAllCategoriesOfBlog(int blogId) {
        return em.createQuery("select c from Category c where c.blog.id = :blog_id")
            .setParameter("blog_id", blogId)
            .getResultList();
    }

    // blog의 주인인 member가 가지고 있는 모든 최신글 중 5개만 가져온다
    public List<Article> getAllArticlesOfBlog(int blogId) {
        Blog blog = blogRepository.findBlog(blogId);
        List<Article> articles = em.createQuery("select a from Article a"
            +" join fetch a.member m" +
            " join fetch a.category c" +
            " where a.member.id = :member_id order by a.id desc", Article.class)
            .setParameter("member_id", blog.getMember().getId())
            .setFirstResult(0)
            .setMaxResults(5)
            .getResultList();
        return articles;
    }

    // 글 목록 페이징 기능 있는 메서드 -- 메인에서만 사용, 전체글이 5개 단위로 페이징됨
    // 글 제목을 눌렀을 때 그 글로 이동해야 할 것 같아서 글 객체를 일단 다 넘겨주기로 정함
    public List<Article> getAllArticlesOfBlog(int blogId, int page) {
        Blog blog = blogRepository.findBlog(blogId);
        List<Article> articles = em.createQuery("select a from Article a"
            +" join fetch a.member m" +
            " join fetch a.category c" +
            " where a.member.id = :member_id order by a.id desc", Article.class)
            .setParameter("member_id", blog.getMember().getId())
            .setFirstResult(page)
            .setMaxResults(5)
            .getResultList();
        return articles;
    }

}
