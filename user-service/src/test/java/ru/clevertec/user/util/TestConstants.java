package ru.clevertec.user.util;

import java.net.URI;

public interface TestConstants {

    String USERNAME = "username";
    String PASSWORD = "password";
    String FIRST_NAME = "first name";
    String LAST_NAME = "last name";
    Integer ID = 2;
    String TOKEN = "Bearer sometoken";
    String ENCODED_PASSWORD = "ENCODED_PASSWORD";

    String API_PREFIX = "/api/v1";

    String REGISTER_USER_URL = API_PREFIX + "/auth/register";
    String LOGIN_USER_URL = API_PREFIX + "/auth/login";
    String GET_USER_INFO_URL = API_PREFIX + "/users/info";
}
