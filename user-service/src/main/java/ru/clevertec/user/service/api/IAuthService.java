package ru.clevertec.user.service.api;

import ru.clevertec.user.dto.LoginRequest;
import ru.clevertec.user.dto.LoginResponse;
import ru.clevertec.user.dto.UserRegisterRequest;

public interface IAuthService {

    LoginResponse login(LoginRequest request);

    void register(UserRegisterRequest request);
}
