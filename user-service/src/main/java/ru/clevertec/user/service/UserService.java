package ru.clevertec.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.user.dto.UserRegisterRequest;
import ru.clevertec.user.model.User;
import ru.clevertec.user.repository.UserRepository;
import ru.clevertec.user.security.CustomUserDetails;
import ru.clevertec.user.service.api.IUserService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User create(UserRegisterRequest request) {
        User user = modelMapper.map(request, User.class);
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        return new CustomUserDetails(user);
    }
}
