package projectBlog.customBlog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Comment;
import projectBlog.customBlog.repository.ArticleRepository;
import projectBlog.customBlog.repository.CommentRepository;
import projectBlog.customBlog.repository.CrudRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final CrudRepository crudRepository;

    public void postChildComment(int parentId, String content) {
        Comment parent = commentRepository.findComment(parentId);
        Comment child = Comment.makeChildComment(content);
        try {
            parent.addChildComment(child);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postParentComment(int articleId, String content) {
        Article article = articleRepository.findArticle(articleId);
        Comment comment = Comment.makeParentComment(content, article);
        crudRepository.save(comment);
    }

}
