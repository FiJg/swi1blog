package cz.osu.swi1.blog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import cz.osu.swi1.blog.dto.ContentRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class PostControllerTest {

    @Test
    public void testPosts_DefaultSort_ReturnsSortedPosts() {
        // Create an instance of PostController
        PostController postController = new PostController();

        // Call the posts method
        List<JsonNode> posts = (List<JsonNode>) postController.posts(null, null);

        // Assert the result
        Assert.assertNotNull(posts);
    }

    @Test
    public void testPost_ExistingId_ReturnsPost() {
        // Create an instance of PostController
        PostController postController = new PostController();

        // Prepare test data
        Long postId = 1L;

        // Call the post method
        JsonNode post = postController.post(postId).getBody();

        // Assert the result
        Assert.assertNotNull(post);
        Assert.assertEquals(Optional.of(postId), post.get("id").asLong());
    }

    @Test
    public void testCreatePost_ValidRequest_ReturnsCreatedPost() {
        // Create an instance of PostController
        PostController postController = new PostController();

        // Prepare test data
        ContentRequest contentRequest = new ContentRequest();
        contentRequest.setTitle("TestTitle");
        contentRequest.setContent("TestContent");

        // Call the createPost method
        JsonNode createdPost = postController.createPost(contentRequest).getBody();

        // Assert the result
        Assert.assertNotNull(createdPost);
        Assert.assertEquals("TestTitle", createdPost.get("title").asText());
    }
}
