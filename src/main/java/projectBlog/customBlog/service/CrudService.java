package projectBlog.customBlog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectBlog.customBlog.repository.CrudRepository;

@Service
@RequiredArgsConstructor
public class CrudService {

    private final CrudRepository crudRepository;

    public void save(Object object) {
        crudRepository.save(object);
        System.out.println(object.getClass());
    }

}
