package cz.osu.swi1.blog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import cz.osu.swi1.blog.dto.ContentRequest;
import cz.osu.swi1.blog.entity.Comment;
import cz.osu.swi1.blog.repository.CommentRepository;
import cz.osu.swi1.blog.service.AuthService;
import cz.osu.swi1.blog.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;

    public CommentController(PostRepository postRepository, CommentRepository commentRepository, AuthService authService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.authService = authService;
    }

    @PostMapping("/comments")
    public ResponseEntity<JsonNode> addComment(@RequestBody ContentRequest contentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentRepository.save(createNewComment(contentRequest)).asJson());
    }

    private Comment createNewComment(ContentRequest contentRequest) {
        return new Comment(
                contentRequest.getContent(),
                LocalDate.now(),
                authService.getBlogUser().orElseThrow(),
                postRepository.getById(contentRequest.getId())
        );
    }

    @PutMapping("/comments")
    public ResponseEntity<JsonNode> updateComment(@RequestBody ContentRequest contentRequest) {
        return commentRepository.findById(contentRequest.getId())
                .map(comment -> comment.updateContent(contentRequest.getContent()))
                .map(commentRepository::save)
                .map(Comment::asJson)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
