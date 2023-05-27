package ru.clevertec.newssystem.service.api;

import ru.clevertec.newssystem.dto.NewsRequest;
import ru.clevertec.newssystem.dto.NewsResponse;

public interface INewsService<ID extends Number> extends RUDService<NewsRequest, NewsResponse, ID> {

    NewsResponse create(NewsRequest request);
}
