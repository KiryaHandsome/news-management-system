package ru.clevertec.newssystem.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newssystem.dto.NewsRequest;
import ru.clevertec.newssystem.dto.NewsResponse;
import ru.clevertec.newssystem.exception.EntityNotFoundException;
import ru.clevertec.newssystem.model.News;
import ru.clevertec.newssystem.repository.NewsRepository;
import ru.clevertec.newssystem.service.api.INewsService;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsService implements INewsService<Integer> {

    private final ModelMapper mapper;
    private final NewsRepository newsRepository;

    @Override
    public NewsResponse create(NewsRequest request) {
        News news = mapper.map(request, News.class);
        News createdNews = newsRepository.save(news);
        return mapper.map(createdNews, NewsResponse.class);
    }

    @Override
    public NewsResponse find(Integer id) {
        return newsRepository.findById(id)
                .map(n -> mapper.map(n, NewsResponse.class))
                .orElseThrow(() -> new EntityNotFoundException(id, "News with such id not found."));
    }

    @Override
    public Page<NewsResponse> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .map(n -> mapper.map(n, NewsResponse.class));
    }

    @Override
    public NewsResponse update(Integer id, NewsRequest request) {
        News news = mapper.map(request, News.class);
        News updatedNews = newsRepository.save(news);
        return mapper.map(updatedNews, NewsResponse.class);
    }

    @Override
    public void delete(Integer id) {
        newsRepository.deleteById(id);
    }
}
