package services.Category;

import entities.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAllCategory() throws Exception;

    void createCategory(String name) throws Exception;
}
