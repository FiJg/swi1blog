package cz.osu.swi1.blog.repository;

import cz.osu.swi1.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
