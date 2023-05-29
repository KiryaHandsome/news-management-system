package ru.clevertec.newssystem.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.newssystem.dto.news.NewsDTO;
import ru.clevertec.newssystem.dto.news.NewsRequest;
import ru.clevertec.newssystem.dto.news.NewsResponse;

public interface INewsService<ID extends Number> {

    NewsResponse find(ID id, Pageable pageable);

    Page<NewsDTO> findAll(Pageable pageable);

    NewsDTO update(ID id, NewsRequest entity);

    void delete(ID id);

    NewsDTO create(NewsRequest request);
}
