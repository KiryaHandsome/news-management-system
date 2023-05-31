package ru.clevertec.newssystem.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.newssystem.dto.news.NewsDTO;
import ru.clevertec.newssystem.dto.news.NewsRequest;
import ru.clevertec.newssystem.dto.news.NewsResponse;

public interface INewsService {

    NewsResponse find(Integer id);

    Page<NewsDTO> findAll(String text, String title, Pageable pageable);

    NewsResponse update(Integer id, NewsRequest entity);

    void delete(Integer id);

    NewsResponse create(NewsRequest request);
}
