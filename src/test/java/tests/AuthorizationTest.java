package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.AuthorizationRequest;
import core.models.AuthorizationResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(AllureJunit5.class)
@Epic("Авторизация")
@Feature("Проверка успешной авторизации")
public class AuthorizationTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private AuthorizationRequest authorizationRequest;
    private AuthorizationResponse authorizationResponse;

    @Step("Установка логина и пароля")
    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setEmail("qwerty@example.com");
        authorizationRequest.setPassword("Password123");
    }

    @Step("Отправка POST-запроса на /auth/login для авторизации пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testAuthorization() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(authorizationRequest);
        Response response = apiClient.authorize(requestBody);
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.asString();
        authorizationResponse = objectMapper.readValue(responseBody, AuthorizationResponse.class);

        assertThat(authorizationResponse.getToken()).isNotEmpty();
        assertThat(authorizationResponse.getUser().getId()).isGreaterThan(0);
        assertThat(authorizationResponse.getUser().getUsername()).isEqualTo("qwerty123");
        assertThat(authorizationResponse.getUser().getEmail()).isEqualTo("qwerty@example.com");
        assertThat(authorizationResponse.getUser().getRole()).isEqualTo("USER");
        assertThat(authorizationResponse.getUser().getFirst_name()).isEqualTo("Иван");
        assertThat(authorizationResponse.getUser().getLast_name()).isEqualTo("Иванов");
        assertThat(authorizationResponse.getUser().getPhone()).isEqualTo("+7 (999) 123-45-67");
        assertThat(authorizationResponse.getUser().getIs_active()).isEqualTo(true);
        assertThat(authorizationResponse.getUser().getCreated_at()).isEqualTo("2026-01-09T02:03:54.475Z");
        assertThat(authorizationResponse.getUser().getUpdated_at()).isEqualTo("2026-01-09T02:03:54.475Z");
    }
}
