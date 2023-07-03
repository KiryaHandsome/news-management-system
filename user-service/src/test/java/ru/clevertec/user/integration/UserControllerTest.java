package ru.clevertec.user.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.user.model.Role;
import ru.clevertec.user.util.TestConstants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = TestConstants.USERNAME, roles = "JOURNALIST")
    void checkGetUserInfoShouldReturnExpectedUserInfoAndStatus200() throws Exception {

        mockMvc.perform(get(TestConstants.GET_USER_INFO_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(TestConstants.USERNAME))
                .andExpect(jsonPath("$.authorities.length()").value(1))
                .andExpect(jsonPath("$.authorities[0]").value(Role.ROLE_JOURNALIST.name()));
    }

    @Test
    @WithAnonymousUser
    void checkGetUserInfoShouldStatus403() throws Exception {
        mockMvc.perform(get(TestConstants.GET_USER_INFO_URL))
                .andExpect(status().isForbidden());
    }
}