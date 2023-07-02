package ru.clevertec.user.service.api;

import ru.clevertec.user.dto.RegisterRequest;
import ru.clevertec.user.model.User;

public interface IUserService {

    User save(RegisterRequest request);
}
