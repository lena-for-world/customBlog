package projectBlog.customBlog.service;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Status;
import projectBlog.customBlog.repository.ArticleRepository;
import projectBlog.customBlog.repository.CategoryRepository;
import projectBlog.customBlog.repository.CrudRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CategoryService{

    private final CrudRepository crudRepository;
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;


    /** 카테고리 추가 로직
     컨트롤러 단에서 생성한 카테고리를 저장한다
     컨트롤러 단에서 부모, 자식 여부에 따라 카테고리를 생성하기 때문에 저장만 하면 된다
      -> 카테고리 하단에서 추가 버튼을 눌렀을 때는 무조건 부모 카테고리가 생성되게 한다
     -> 카테고리의 측면에서 추가 버튼을 눌렀을 때는 무조건 자식 카테고리가 생성되게 한다
     */
    public void saveCategory(Category category) {
        crudRepository.save(category);
    }


    /** 카테고리 이름 수정 */
    public void updateCategory(int cateId, String cateName) {
        // 이름 중복 체크
        Category category = categoryRepository.findCategory(cateId);
        log.info(cateName);
        category.editContent(cateName);
        log.info(category.getName());
    }


    /** 카테고리 이동 -- 자식 카테고리의 부모를 변경 */
    public void moveCategory(int articleId, int cateId) {
        Article article = articleRepository.findArticle(articleId);
        Category moveCategory = categoryRepository.findCategory(cateId);
        article.moveArticleCategory(moveCategory);
    }


    /** 카테고리 삭제 로직
     받아온 카테고리 id로 카테고리를 찾고, 해당 카테고리가 부모인지 자식인지에 따라 처리한다
     부모 자식에 관계없이 글이 있다면 삭제하지 못한다
     부모일 경우 자식 카테고리가 있다면 삭제하지 못한다
     */
    public void categoryDelete(int cateId) {

        Category category = categoryRepository.findCategory(cateId);
        crudRepository.delete(category);

    }

}
