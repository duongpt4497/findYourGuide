package services.Category;

import entities.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    public void createCategory(Category newCategory);

    public List<Category> findCategory();

    public void editCategory(Category categoryNeedUpdate);
}
