package ru.clevertec.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exception.IncorrectPasswordException;
import ru.clevertec.logging.annotation.Loggable;
import ru.clevertec.user.dto.LoginRequest;
import ru.clevertec.user.dto.LoginResponse;
import ru.clevertec.user.dto.RegisterRequest;
import ru.clevertec.user.model.User;
import ru.clevertec.user.repository.UserRepository;
import ru.clevertec.user.service.api.IAuthService;

@Slf4j
@Service
@Loggable
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username '%s' not found", request.getUsername())
                ));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Wrong password.");
        }
        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
