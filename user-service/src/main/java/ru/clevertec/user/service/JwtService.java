package ru.clevertec.user.service;

import org.springframework.stereotype.Service;
import ru.clevertec.user.model.User;
import ru.clevertec.user.service.api.IJwtService;

@Service
public class JwtService implements IJwtService {

    @Override
    public String extractUsername(String jwt) {
        return null;
    }

    @Override
    public String generateToken(User user) {
        return null;
    }

    @Override
    public boolean isValid(String jwt) {
        return false;
    }
}
