package cz.osu.swi1.blog;

import cz.osu.swi1.blog.controller.CategoryController;
import cz.osu.swi1.blog.dto.CategoryRequest;
import cz.osu.swi1.blog.entity.Category;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CategoryControllerTest {

    @Test
    public void testGetCategories_ReturnsCategoriesList() {
        // Create an instance of CategoryController
        CategoryController categoryController = new CategoryController();

        // Call the getCategories method
        List<Category> categories = (List<Category>) categoryController.getCategories();

        // Assert the result
        Assert.assertNotNull(categories);
    }

    @Test
    public void testGetCategoryById_ExistingId_ReturnsCategory() {
        // Create an instance of CategoryController
        CategoryController categoryController = new CategoryController();

        // Prepare test data
        Long categoryId = 1L;

        // Call the getCategoryById method
        Category category = categoryController.getCategoryById(categoryId).getBody();

        // Assert the result
        Assert.assertNotNull(category);
        Assert.assertEquals(categoryId, category.getId());
    }

    @Test
    public void testCreateCategory_ValidRequest_ReturnsCreatedCategory() {
        // Create an instance of CategoryController
        CategoryController categoryController = new CategoryController();

        // Prepare test data
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("TestCategory");

        // Call the createCategory method
        Category createdCategory = categoryController.createCategory(categoryRequest).getBody();

        // Assert the result
        Assert.assertNotNull(createdCategory);
        Assert.assertEquals("TestCategory", createdCategory.getName());
    }
}
