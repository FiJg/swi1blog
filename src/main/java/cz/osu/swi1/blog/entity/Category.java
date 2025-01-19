package cz.osu.swi1.blog.entity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;

@Entity
@Getter
@Setter
@ToString
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate createdOn;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "blog_user_id")
    private BlogUser blogUser;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> posts;

    public Category(String name, LocalDate createdOn, BlogUser blogUser, List<Post> posts) {
        this.name = name;
        this.createdOn = createdOn;
        this.blogUser = blogUser;
        this.posts = posts;
    }

    public Category(String name) {

        this.name = name;
    }

    public Category() {

    }

    public Category(String name, List<Post> posts) {
        this.name = name;

        this.posts = posts;
    }

    public Category(String name, BlogUser blogUser) {
        this.name = name;

        this.blogUser = blogUser;
    }


    public JsonNode asJson() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode()
                .put("id", id)
                .put("name", name)
                .put("createdOn", createdOn.toString())
                .put("blogUser", blogUser.getUserName())
                .put("posts", posts.size());
    }

    public JsonNode asJsonWithPosts() {
        return ((ObjectNode) asJson())
                .set("posts", getPosts().stream()
                        .map(Post::asJson)
                        .collect(Collector.of(() -> new ObjectMapper().createArrayNode(), ArrayNode::add, ArrayNode::addAll))
                );
    }


}
