package cz.osu.swi1.blog;

import cz.osu.swi1.blog.controller.CommentController;
import cz.osu.swi1.blog.dto.ContentRequest;
import cz.osu.swi1.blog.entity.BlogUser;
import cz.osu.swi1.blog.entity.Comment;
import cz.osu.swi1.blog.entity.Post;
import cz.osu.swi1.blog.repository.CommentRepository;
import cz.osu.swi1.blog.repository.PostRepository;
import cz.osu.swi1.blog.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentControllerTest {


    private BlogUser blogUser;
    private Post post;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AuthService authService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @BeforeEach
    public void setUp() {
        // other setup...
        blogUser = new BlogUser(); // Initialize it with desired state
        post = new Post(); // Initialize it with desired state

        // When authService.getBlogUser() is called, return an Optional of blogUser
        Mockito.when(authService.getBlogUser()).thenReturn(Optional.of(blogUser));

        // When postRepository.getById() is called, return post
        Mockito.when(postRepository.getById(Mockito.any())).thenReturn(post);
    }



    @Test
    public void testAddComment() {
        ContentRequest contentRequest = new ContentRequest();
        Comment comment = new Comment(contentRequest.getContent(), LocalDate.now(),
                authService.getBlogUser().orElseThrow(), postRepository.getById(contentRequest.getId()));

        Mockito.when(commentRepository.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

        ResponseEntity response = commentController.addComment(contentRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testUpdateComment() {
        ContentRequest contentRequest = new ContentRequest();
        Comment comment = new Comment(contentRequest.getContent(), LocalDate.now(),
                authService.getBlogUser().orElseThrow(), postRepository.getById(contentRequest.getId()));

        Mockito.when(commentRepository.findById(Mockito.any())).thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

        ResponseEntity response = commentController.updateComment(contentRequest);
        assertTrue(response.hasBody());
    }

    @Test
    public void testDeleteComment() {
        ResponseEntity<HttpStatus> response = commentController.deleteComment(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
