package ru.clevertec.newssystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.clevertec.newssystem.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findAll(Pageable pageable);

}
