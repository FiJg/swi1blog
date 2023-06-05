package cz.osu.swi1.blog;

import cz.osu.swi1.blog.controller.CategoryController;
import cz.osu.swi1.blog.dto.CategoryRequest;
import cz.osu.swi1.blog.entity.Category;
import cz.osu.swi1.blog.repository.CategoryRepository;
import cz.osu.swi1.blog.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryControllerTest {

    private CategoryController categoryController;
    private CategoryRepository categoryRepository;
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        authService = Mockito.mock(AuthService.class);
        categoryController = new CategoryController(categoryRepository, authService);
    }

    @Test
    public void testGetCategories_ReturnsCategoriesList() {
        Mockito.when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Category>> responseEntity = categoryController.getCategories();
        List<Category> categories = responseEntity.getBody();

        assertNotNull(categories);
        assertEquals(0, categories.size());
    }

    @Test
    public void testGetCategoryById_ExistingId_ReturnsCategory() {

        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryController.getCategoryById(categoryId).getBody();

        assertNotNull(result);
        assertEquals(categoryId, result.getId());
    }

    @Test
    public void testCreateCategory_ValidRequest_ReturnsCreatedCategory() {

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("TestCategory");

        Category testCategory = new Category();
        testCategory.setName("TestCategory");

        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(testCategory);

        Category createdCategory = categoryController.createCategory(categoryRequest).getBody();

        assertNotNull(createdCategory);
        assertEquals("TestCategory", createdCategory.getName());
    }
}
