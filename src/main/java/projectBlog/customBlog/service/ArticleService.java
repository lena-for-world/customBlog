package projectBlog.customBlog.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.repository.ArticleRepository;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    // 수정, 카테고리 이동 - 멤버함수로 구현

    public List<Article> findRecentFiveArticles() {
        return articleRepository.findRecentFiveArticles();
    }



}
