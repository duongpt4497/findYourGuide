package winter.findGuider.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.Category.CategoryService;

@RestController
@RequestMapping(path = "/Category", produces = "application/json")
@CrossOrigin(origins = "*")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService cs) {
        this.categoryService = cs;
    }

}
