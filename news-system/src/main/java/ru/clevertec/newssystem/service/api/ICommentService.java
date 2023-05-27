package ru.clevertec.newssystem.service.api;

import ru.clevertec.newssystem.dto.CommentRequest;
import ru.clevertec.newssystem.dto.CommentResponse;

public interface ICommentService<ID extends Number> extends RUDService<CommentRequest, CommentResponse, ID> {

    CommentResponse create(ID newsId, CommentRequest request);
}
