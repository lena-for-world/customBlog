package projectBlog.customBlog.service;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Status;
import projectBlog.customBlog.repository.CrudRepository;

@Service
@RequiredArgsConstructor
public class CategoryService{

    private final EntityManager em;
    private final CrudRepository crudRepository;

    public List<Article> ArticlesOfCategory(int cateId) {
        return em.createQuery("select a from Article a where a.category.id = :cate_id")
            .setParameter("cate_id", cateId)
            .getResultList();
    }

    // 어떤 카테고리를 누르면 해당 카테고리에 해당하는 글들만 가져온다
    // 최신 순으로 정렬한다
    // 첫 화면에서 5개 글만 가져온다
    public List<Article> getFiveArticlesFromTheCategory(int blogId, int cateId) {
        return em.createQuery("select a from Article a"
            + " where a.member.blog.id = :blog_id and a.category.id = :category_id")
            .setParameter("blog_id", blogId)
            .setParameter("category_id", cateId)
            .setFirstResult(0)
            .setMaxResults(5)
            .getResultList();
    }

    // 위와 동일한 기능에 페이징 추가
    public List getFiveArticlesFromTheCategory(int blogId, int cateId, int page) {
        return em.createQuery("select a from Article a"
            + " where a.member.blog.id = :blog_id and a.category.id = :category_id")
            .setParameter("blog_id", blogId)
            .setParameter("category_id", cateId)
            .setFirstResult(page*5)
            .setMaxResults(5)
            .getResultList();
    }

    // 카테고리 삭제 로직
    // 받아온 카테고리 id로 카테고리를 찾고, 해당 카테고리가 부모인지 자식인지에 따라 처리한다
    // 부모 자식에 관계없이 글이 있다면 삭제하지 못한다
    // 부모일 경우 자식 카테고리가 있다면 삭제하지 못한다
    public void categoryDelete(int cateId) {
        Category category = em.find(Category.class, cateId);
        if(category.getArticles().size() > 0) {
            // 에러 처리! 글이 포함되어 있으므로 삭제할 수 없는 카테고리
        } else {
            if(category.getStatus() == Status.Parent && category.getChilds().size() > 0) {
                // 부모 카테고리이고 자식이 있을 경우, 삭제할 수 없음
            } else {
                crudRepository.delete(category);
            }
        }
    }

    // 카테고리 추가 로직
    // 컨트롤러 단에서 생성한 카테고리를 저장한다
    // 컨트롤러 단에서 부모, 자식 여부에 따라 카테고리를 생성하기 때문에 저장만 하면 된다
    // -> 카테고리 하단에서 추가 버튼을 눌렀을 때는 무조건 부모 카테고리가 생성되게 한다
    // -> 카테고리의 측면에서 추가 버튼을 눌렀을 때는 무조건 자식 카테고리가 생성되게 한다
    public void categorySave(Category category) {
        crudRepository.save(category);
    }

    // 카테고리 이름 수정
    public void updateCategory(int cateId, String cateName) {
        Category category = em.find(Category.class, cateId);
        category.editContent(cateName);
    }

    // 카테고리 이동 -- 자식 카테고리의 부모를 변경

}
