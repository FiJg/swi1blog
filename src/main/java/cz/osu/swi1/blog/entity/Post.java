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

@Getter
@Setter
@ToString
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;
    private LocalDate createdOn;
    private LocalDate updatedOn;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "blog_user_id")
    private BlogUser blogUser;
    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "category_id")
    private Category category;

    public Post() {
    }

    public Post(String title, String content, LocalDate createdOn, LocalDate updatedOn, BlogUser blogUser, List<Comment> comments) {
        this.title = title;
        this.content = content;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.blogUser = blogUser;
        this.comments = comments;
    }

    public Post(String title, String content, LocalDate createdOn, LocalDate updatedOn, BlogUser blogUser, List<Comment> comments, Category category) {
        this.title = title;
        this.content = content;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.blogUser = blogUser;
        this.comments = comments;
        this.category = category;
    }

    public Post updateContent(String updatedContent) {
        setContent(updatedContent);
        setUpdatedOn(LocalDate.now());
        return this;
    }


    public JsonNode asJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode postNode = mapper.createObjectNode();
        postNode.put("id", id);
        postNode.put("title", title);
        postNode.put("content", content);
        postNode.put("createdOn", createdOn.toString());
        postNode.put("updatedOn", updatedOn.toString());
        postNode.put("blogUser", blogUser.getUserName());
        postNode.put("comments", comments.size());

        if (category != null) {
            postNode.put("category", category.getName());
        } else {
            postNode.putNull("category");
        }

        return postNode;
    }

    public JsonNode asJsonWithComments() {
        return ((ObjectNode) asJson())
                .set("comments", getComments().stream()
                        .map(Comment::asJson)
                        .collect(Collector.of(() -> new ObjectMapper().createArrayNode(), ArrayNode::add, ArrayNode::addAll))
                );
    }

    @Override
    public String toString() {
        return "Post{" + "title='" + title + '\'' + ", content='" + content + '\'' + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + ", blogUser=" + blogUser.getUserName() + ", comments=" + comments.size() + '}';
    }
}
