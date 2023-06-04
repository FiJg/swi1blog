package cz.osu.swi1.blog.entity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;

@Entity
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

    @OneToMany(mappedBy = "category", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Post> posts;

    public Category() {
    }

    public Category(String name, LocalDate createdOn, BlogUser blogUser, List<Post> posts) {
        this.name = name;
        this.createdOn = createdOn;
        this.blogUser = blogUser;
        this.posts = posts;
    }

    public Category(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public BlogUser getBlogUser() {
        return blogUser;
    }

    public void setBlogUser(BlogUser blogUser) {
        this.blogUser = blogUser;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
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

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdOn=" + createdOn +
                ", blogUser=" + blogUser.getUserName() +
                ", posts=" + posts.size() +
                '}';
    }
}
