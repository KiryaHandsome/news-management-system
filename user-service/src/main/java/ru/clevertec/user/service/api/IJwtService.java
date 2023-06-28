package ru.clevertec.user.service.api;

import ru.clevertec.user.model.User;

/**
 * Interface for working with JWT.
 */
public interface IJwtService {

    String extractUsername(String jwt);

    String generateToken(User user);

    boolean isExpired(String jwt);
}
