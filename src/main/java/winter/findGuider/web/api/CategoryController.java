package winter.findGuider.web.api;

import entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Category.CategoryService;

@RestController
@RequestMapping(path = "/category", produces = "application/json")
@CrossOrigin(origins = "*")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping("/findAll")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Category> findAllCategory() {
        try {
            return new ResponseEntity(categoryService.findAllCategory(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> createCategory(@RequestParam String name) {
        try {
            categoryService.createCategory(name);
            return new ResponseEntity(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(false, HttpStatus.NOT_FOUND);
        }
    }
}
