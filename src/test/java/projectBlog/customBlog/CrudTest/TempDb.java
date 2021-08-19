package projectBlog.customBlog.CrudTest;

import java.util.ArrayList;
import java.util.List;
import projectBlog.customBlog.domain.Article;

public class TempDb {

    List<Article> list = new ArrayList<>();

    public void save(Article article) {
        list.add(article);
    }

}