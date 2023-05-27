package ru.clevertec.newssystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.newssystem.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
