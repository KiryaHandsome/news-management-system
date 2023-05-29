package ru.clevertec.newssystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.newssystem.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("""
            SELECT c FROM Comment c
            WHERE (:comment IS NULL OR c.text ILIKE %:comment%)
            """)
    Page<Comment> findAll(@Param("comment") String comment, Pageable pageable);

    Page<Comment> findByNewsId(Integer newsId, Pageable pageable);
}
