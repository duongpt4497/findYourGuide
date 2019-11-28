package services.Category;

import entities.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAllCategory();

    void createCategory(String name);
}
