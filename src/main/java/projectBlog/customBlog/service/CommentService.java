package projectBlog.customBlog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Comment;
import projectBlog.customBlog.domain.Member;
import projectBlog.customBlog.domain.Status;
import projectBlog.customBlog.repository.ArticleRepository;
import projectBlog.customBlog.repository.CommentRepository;
import projectBlog.customBlog.repository.CrudRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final CrudRepository crudRepository;
    private final MemberService memberService;

   public void postChildComment(int parentId, String content) {
        Comment parent = commentRepository.findComment(parentId);
        Comment child = Comment.makeChildComment(content);
        try {
            parent.addChildComment(child);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postParentComment(int articleId, String content, int memberId) {
        Article article = articleRepository.findArticle(articleId);
        Member member = memberService.getMember(memberId);
        Comment comment = Comment.makeParentComment(content, article, member);
        crudRepository.save(comment);
    }

    // 댓글 수정 기능
   public void editComment(int commentId, String content) {
       Comment findComment = commentRepository.findComment(commentId);
       findComment.editContent(content);
   }

   // 댓글 삭제
   public void deleteComment(int commentId) {

       Comment findComment = commentRepository.findComment(commentId);
       Article article = findComment.getArticle();

       if(findComment.getStatus() == Status.Parent) {
           article.deleteComment(findComment);
           crudRepository.delete(findComment);
       } else {
           Comment parentComment = findComment.getParent();
           parentComment.removeConnectionFromParent(findComment);
           crudRepository.delete(findComment);
       }
   }
}
