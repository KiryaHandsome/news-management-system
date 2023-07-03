package ru.clevertec.user.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.exception.IncorrectPasswordException;
import ru.clevertec.user.dto.LoginRequest;
import ru.clevertec.user.dto.RegisterRequest;
import ru.clevertec.user.service.AuthService;
import ru.clevertec.user.util.TestConstants;
import ru.clevertec.user.util.TestData;

import java.util.stream.Stream;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest extends BaseIntegrationTest {

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    class RegisterTest {

        @Test
        void shouldReturnStatus201() throws Exception {
            var registerRequest = TestData.getRegisterRequest();
            String requestBody = objectMapper.writeValueAsString(registerRequest);

            mockMvc.perform(post(TestConstants.REGISTER_USER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            verify(authService).register(TestData.getRegisterRequest());
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.user.integration.AuthControllerTest#provideExceptions")
        void shouldThrowIncorrectPasswordExceptionAndReturnStatus401(Exception exception) throws Exception {
            var registerRequest = TestData.getRegisterRequest();
            String requestBody = objectMapper.writeValueAsString(registerRequest);

            doThrow(exception)
                    .when(authService)
                    .register(registerRequest);

            mockMvc.perform(post(TestConstants.REGISTER_USER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            verify(authService).register(TestData.getRegisterRequest());
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.user.integration.AuthControllerTest#provideBadRegisterRequests")
        void shouldReturnStatus400(RegisterRequest registerRequest) throws Exception {
            String requestBody = objectMapper.writeValueAsString(registerRequest);

            mockMvc.perform(post(TestConstants.REGISTER_USER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }


    @Nested
    class LoginTest {

        @ParameterizedTest
        @MethodSource("ru.clevertec.user.integration.AuthControllerTest#provideBadLoginRequests")
        void shouldReturnStatus400(LoginRequest loginRequest) throws Exception {
            String requestBody = objectMapper.writeValueAsString(loginRequest);

            mockMvc.perform(post(TestConstants.LOGIN_USER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        void shouldReturnAccessTokenAndStatus200() throws Exception {
            var loginRequest = TestData.getLoginRequest();
            String requestBody = objectMapper.writeValueAsString(loginRequest);

            doReturn(TestData.getLoginResponse())
                    .when(authService)
                    .login(loginRequest);

            mockMvc.perform(post(TestConstants.LOGIN_USER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.accessToken").value(TestConstants.TOKEN));

            verify(authService).login(TestData.getLoginRequest());
        }
    }

    private static Stream<Exception> provideExceptions() {
        return Stream.of(
                new IncorrectPasswordException("message"),
                new UsernameNotFoundException("message")
        );
    }

    private static Stream<LoginRequest> provideBadLoginRequests() {
        return Stream.of(
                TestData.getLoginRequest().setUsername(null),
                TestData.getLoginRequest().setUsername(""),
                TestData.getLoginRequest().setUsername("  "),
                TestData.getLoginRequest().setPassword(null),
                TestData.getLoginRequest().setPassword(""),
                TestData.getLoginRequest().setPassword("  ")
        );
    }

    private static Stream<RegisterRequest> provideBadRegisterRequests() {
        return Stream.of(
                TestData.getRegisterRequest().setUsername(null),
                TestData.getRegisterRequest().setUsername(""),
                TestData.getRegisterRequest().setUsername("  "),
                TestData.getRegisterRequest().setUsername("1234567"),
                TestData.getRegisterRequest().setPassword(null),
                TestData.getRegisterRequest().setPassword(""),
                TestData.getRegisterRequest().setPassword("  "),
                TestData.getRegisterRequest().setPassword("1234567"),
                TestData.getRegisterRequest().setFirstName(null),
                TestData.getRegisterRequest().setFirstName(""),
                TestData.getRegisterRequest().setFirstName("  "),
                TestData.getRegisterRequest().setFirstName("1"),
                TestData.getRegisterRequest().setLastName(null),
                TestData.getRegisterRequest().setLastName(""),
                TestData.getRegisterRequest().setLastName("  "),
                TestData.getRegisterRequest().setLastName("1")
        );
    }
}