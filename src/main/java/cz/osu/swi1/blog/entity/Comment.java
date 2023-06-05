package cz.osu.swi1.blog.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String comment;
    private LocalDate createdOn;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "blog_user_id")
    private BlogUser blogUser;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment() {
    }

    public Comment(String comment, LocalDate createdOn, BlogUser blogUser, Post post) {
        this.comment = comment;
        this.createdOn = createdOn;
        this.blogUser = blogUser;
        this.post = post;
    }

    public Comment updateContent(String content) {
        setComment(content);
        setCreatedOn(LocalDate.now());
        return this;
    }


    public JsonNode asJson() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode()
                .put("id", id)
                .put("comment", comment)
                .put("createdOn", createdOn.toString())
                .put("blogUser", blogUser.getUserName());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", createdOn=" + createdOn +
                ", blogUser=" + blogUser.getUserName() +
                ", post=" + post.getTitle() +
                '}';
    }
}
