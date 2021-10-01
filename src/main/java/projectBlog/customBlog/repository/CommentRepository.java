package projectBlog.customBlog.repository;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Comment;

@Repository
@Transactional
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public Comment findComment(int commentId) {
        return em.find(Comment.class, commentId);
    }
}
