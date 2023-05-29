package ru.clevertec.newssystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.newssystem.model.News;

public interface NewsRepository extends JpaRepository<News, Integer> {

    @Query("""
            SELECT n FROM News n
            WHERE (:text IS NULL OR n.text ILIKE %:text%)
            AND (:title IS NULL OR n.title ILIKE %:title%)
            """)
    Page<News> findAll(@Param("text") String text,
                       @Param("title") String title,
                       Pageable pageable);
}
