package winter.findGuider.web.api;

import entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.Category.CategoryServiceImpl;

@RestController
@RequestMapping(path = "/category", produces = "application/json")
@CrossOrigin(origins = "*")
public class CategoryApi {

    private CategoryServiceImpl categoryService;

    @Autowired
    public CategoryApi(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<Category> findAllCategory() {
        try {

            return new ResponseEntity(categoryService.findAllCategory(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace());
        }
        return null;
    }
}
