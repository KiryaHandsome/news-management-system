package ru.clevertec.newssystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.newssystem.model.News;

public interface NewsRepository extends JpaRepository<News, Integer> {

}
