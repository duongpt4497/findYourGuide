package winter.findGuider.web.api;

import entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Category.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/Category", produces = "application/json")
@CrossOrigin(origins = "*")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService cs) {
        this.categoryService = cs;
    }

    @RequestMapping("/Create/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Category>> createCategory(@PathVariable("name") String name) {
        Category newCategory = new Category();
        try {
            newCategory.setName(name);
            categoryService.createCategory(newCategory);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoryService.findCategory(), HttpStatus.OK);
    }

    @RequestMapping("/Edit/{id}/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Category>> editCategory(@PathVariable("id") long id, @PathVariable("name") String name) {
        Category categoryNeedUpdate = new Category();
        try {
            categoryNeedUpdate.setCategory_id(id);
            categoryNeedUpdate.setName(name);
            categoryService.editCategory(categoryNeedUpdate);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoryService.findCategory(), HttpStatus.OK);
    }
}
