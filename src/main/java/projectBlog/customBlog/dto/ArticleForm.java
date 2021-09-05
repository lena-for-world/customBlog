package projectBlog.customBlog.dto;

import lombok.Data;

@Data
public class ArticleForm {

    private String title;
    private String content;
    private Integer categoryId;

}
