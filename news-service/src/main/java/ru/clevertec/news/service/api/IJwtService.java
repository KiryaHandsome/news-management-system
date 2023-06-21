package ru.clevertec.news.service.api;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {

    UserDetails extractUserDetails(String token);
}
