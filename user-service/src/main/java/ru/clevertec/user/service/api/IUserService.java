package ru.clevertec.user.service.api;

import ru.clevertec.user.dto.UserRegisterRequest;
import ru.clevertec.user.model.User;

public interface IUserService {

    User create(UserRegisterRequest request);
}
