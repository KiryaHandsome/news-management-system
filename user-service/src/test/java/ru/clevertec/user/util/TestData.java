package ru.clevertec.user.util;

import ru.clevertec.user.dto.LoginRequest;
import ru.clevertec.user.dto.LoginResponse;
import ru.clevertec.user.dto.RegisterRequest;
import ru.clevertec.user.model.Role;
import ru.clevertec.user.model.User;

public interface TestData {

    static LoginRequest getLoginRequest() {
        return new LoginRequest(
                TestConstants.USERNAME,
                TestConstants.PASSWORD
        );
    }

    static User getUser() {
        return User.builder()
                .id(TestConstants.ID)
                .lastName(TestConstants.LAST_NAME)
                .firstName(TestConstants.FIRST_NAME)
                .password(TestConstants.PASSWORD)
                .username(TestConstants.USERNAME)
                .role(Role.ROLE_SUBSCRIBER)
                .build();
        }

    static RegisterRequest getRegisterRequest() {
        return new RegisterRequest(
                TestConstants.USERNAME,
                TestConstants.PASSWORD,
                TestConstants.FIRST_NAME,
                TestConstants.LAST_NAME
        );
    }

    static LoginResponse getLoginResponse() {
        return new LoginResponse(
                TestConstants.TOKEN
        );
    }
}
