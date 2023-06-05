package cz.osu.swi1.blog.controller;

import cz.osu.swi1.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import com.fasterxml.jackson.databind.JsonNode;
import cz.osu.swi1.blog.dto.ContentRequest;
import cz.osu.swi1.blog.entity.Post;
import cz.osu.swi1.blog.repository.BlogUserRepository;
import cz.osu.swi1.blog.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {
    private PostRepository postRepository;
    private AuthService authService;


    public PostController(
            PostRepository postRepository,
            BlogUserRepository blogUserRepository,
            AuthService authService
    ) {
        this.postRepository = postRepository;
        this.authService = authService;
    }



    @GetMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JsonNode>> posts(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) String searchTerm
    ) {

        //default sort
        Sort.Direction direction = Sort.Direction.DESC;


        if (sortOrder != null && sortOrder.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

        Sort sort = Sort.by(direction, sortBy != null ? sortBy : "createdOn");

        List<Post> allPosts = postRepository.findAll(sort);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            allPosts = allPosts.stream()
                    .filter(post -> post.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(
                postRepository.findAll(sort).stream()
                        .map(Post::asJson)
                        .toList()
        );
    }

    @GetMapping(value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> post(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(Post::asJsonWithComments)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/posts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<JsonNode> createPost(@RequestBody ContentRequest contentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postRepository.save(newPost(contentRequest)).asJson());
    }

    private Post newPost(ContentRequest contentRequest) {
        return new Post(
                contentRequest.getTitle(),
                contentRequest.getContent(),
                LocalDate.now(),
                LocalDate.now(),
                authService.getBlogUser().orElseThrow(),
                new ArrayList<>()
        );
    }

    @PutMapping(value = "/posts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<JsonNode> updatePost(@RequestBody ContentRequest contentRequest) {
        return postRepository.findById(contentRequest.getId())
                .map(post -> post.updateContent(contentRequest.getContent()))
                .map(post -> ResponseEntity.ok(postRepository.save(post).asJson()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/posts/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
