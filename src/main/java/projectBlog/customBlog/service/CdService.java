package projectBlog.customBlog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectBlog.customBlog.repository.CrudRepository;

@Service
@RequiredArgsConstructor
public class CdService {

    private final CrudRepository crudRepository;

    public void save(Object object) {
        crudRepository.save(object);
    }

    public void delete(Object object) {
        crudRepository.delete(object);
    }

}
