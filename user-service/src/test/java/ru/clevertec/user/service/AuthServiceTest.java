package ru.clevertec.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.clevertec.user.repository.UserRepository;
import ru.clevertec.user.util.TestConstants;
import ru.clevertec.user.util.TestData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    private ModelMapper mapper;
    private AuthService authService;


    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        authService = new AuthService(jwtService, mapper, userRepository, passwordEncoder);
    }

    @Test
    void checkLoginShouldReturnObjectWithAccessToken() {
        doReturn(Optional.of(TestData.getUser()))
                .when(userRepository)
                .findByUsername(TestConstants.USERNAME);

        doReturn(TestConstants.TOKEN)
                .when(jwtService)
                .generateToken(TestData.getUser());

        doReturn(true)
                .when(passwordEncoder)
                .matches(TestConstants.PASSWORD, TestConstants.PASSWORD);

        var actual = authService.login(TestData.getLoginRequest());

        assertThat(actual).isNotNull();
        assertThat(actual.getAccessToken()).isEqualTo(TestConstants.TOKEN);

        verify(userRepository).findByUsername(TestConstants.USERNAME);
    }

    @Test
    void checkRegisterShouldCallUserRepositorySave() {
        doReturn(TestConstants.ENCODED_PASSWORD)
                .when(passwordEncoder)
                .encode(TestConstants.PASSWORD);

        authService.register(TestData.getRegisterRequest());

        var user = TestData.getUser();
        user.setId(null);
        user.setPassword(TestConstants.ENCODED_PASSWORD);
        verify(userRepository).save(user);
    }
}