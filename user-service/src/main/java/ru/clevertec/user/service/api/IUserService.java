package ru.clevertec.user.service.api;

import ru.clevertec.user.dto.LoginRequest;
import ru.clevertec.user.dto.LoginResponse;
import ru.clevertec.user.dto.UserRegisterRequest;
import ru.clevertec.user.model.User;

import java.util.Optional;

public interface IUserService {

    User save(UserRegisterRequest request);

    Optional<User> findByUsername(String username);

    LoginResponse login(LoginRequest request);
}
