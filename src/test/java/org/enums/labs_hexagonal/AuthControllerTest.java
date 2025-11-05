package org.enums.labs_hexagonal;



import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request.LoginRequest;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request.SignupRequest;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request.VerifyEmailRequest;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response.AuthResponse;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.AuthController;
import org.enums.labs_hexagonal.application.port.in.AuthUseCase;
import org.enums.labs_hexagonal.domain.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

class AuthControllerTest {

    private AuthUseCase authUseCase;
    private AuthController authController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authUseCase = Mockito.mock(AuthUseCase.class);
        authController = new AuthController(authUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void signup_ReturnsCreatedResponse() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .email("ade@gmail.com")
                .password("password")
                .build();

        AuthResponse response = AuthResponse.builder()
                .email(request.getEmail())
                .message("PENDING_VERIFICATION")
                .build();

        when(authUseCase.signup(any(SignupRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ade@gmail.com"))
                .andExpect(jsonPath("$.message").value("PENDING_VERIFICATION"));

        verify(authUseCase, times(1)).signup(any(SignupRequest.class));
    }

    @Test
    void verifyEmail_ReturnsOk() throws Exception {
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setToken("sample-token");

        doNothing().when(authUseCase).verifyEmail("sample-token");

        mockMvc.perform(post("/v1/auth/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email verified successfully. You can now log in."));

        verify(authUseCase, times(1)).verifyEmail("sample-token");
    }

    @Test
    void login_ReturnsOkResponse() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("ade@gmail.com")
                .password("password")
                .build();

        AuthResponse response = AuthResponse.builder()
                .email("ade@gmail.com")
                .message("LOGIN_SUCCESS")
                .sessionToken("token123")
                .build();

        when(authUseCase.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ade@gmail.com"))
                .andExpect(jsonPath("$.message").value("LOGIN_SUCCESS"))
                .andExpect(jsonPath("$.sessionToken").value("token123"));

        verify(authUseCase, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void logout_WithAuthorizationHeader_ReturnsNoContent() throws Exception {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest();
        httpRequest.addHeader("Authorization", "Bearer token123");

        doNothing().when(authUseCase).logout("token123");

        mockMvc.perform(post("/v1/auth/logout")
                        .header("Authorization", "Bearer token123"))
                .andExpect(status().isNoContent());

        verify(authUseCase, times(1)).logout("token123");
    }

    @Test
    void logout_WithoutAuthorizationHeader_ReturnsNoContent() throws Exception {
        mockMvc.perform(post("/v1/auth/logout"))
                .andExpect(status().isNoContent());

        verify(authUseCase, never()).logout(any());
    }

    @Test
    void handleException_EmailAlreadyExists_ReturnsBadRequest() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .email("ade@gmail.com")
                .password("password")
                .build();

        when(authUseCase.signup(any(SignupRequest.class)))
                .thenThrow(new EmailAlreadyExistsException());

        mockMvc.perform(post("/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("EMAIL_IN_USE"));
    }
}
