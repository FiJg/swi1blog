package cz.osu.swi1.blog.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "blog_user")
public class BlogUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")

    private Long id;
    @Column(name = "username")
    private String userName;
    @Column(name = "authority")
    private String authority;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;


    @OneToMany(mappedBy = "blogUser", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> posts;
    @OneToMany(mappedBy = "blogUser", cascade = {CascadeType.ALL},orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;



    public BlogUser(String userName, String authority, String password, String email) {
        this.userName = userName;
        this.authority = authority;
        this.password = password;
        this.email = email;
        this.posts = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public List<Post> getPosts() {
        return posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    @Override
    public String toString() {
        return "BlogUser{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", posts=" + posts.size() +
                ", comments=" + comments.size() +
                '}';
    }
}
