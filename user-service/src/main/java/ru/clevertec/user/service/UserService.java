package ru.clevertec.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.user.dto.LoginRequest;
import ru.clevertec.user.dto.LoginResponse;
import ru.clevertec.user.dto.UserRegisterRequest;
import ru.clevertec.user.model.User;
import ru.clevertec.user.repository.UserRepository;
import ru.clevertec.user.security.CustomUserDetails;
import ru.clevertec.user.service.api.IUserService;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User save(UserRegisterRequest request) {
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username '%s' not found", username)
                ));
        return new CustomUserDetails(user);
    }
}
