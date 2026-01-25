package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.RegistrationRequest;
import core.models.RegistrationResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(AllureJunit5.class)
@Epic("Регистрация")
@Feature("Проверка успешной регистрации")
public class RegistrationTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private RegistrationRequest registrationRequest;
    private RegistrationResponse registrationResponse;

    @Step("Подготовка данных для регистрации")
    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        registrationRequest = new RegistrationRequest();
        registrationResponse = new RegistrationResponse();

        registrationRequest.setUsername("AndreyBlinov1");
        registrationRequest.setEmail("andrey.blinov1@mail.ru");
        registrationRequest.setPassword("qwerty1234");
        registrationRequest.setSecret_code("TESTCODE2025");
        registrationRequest.setFirst_name("Андрей");
        registrationRequest.setLast_name("Блинов");
        registrationRequest.setPhone("+7 (999) 100-10-11");
    }

    @Step("Отправка POST-запроса на /users на регистрацию пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testRegistration() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(registrationRequest);
        Response response = apiClient.register(requestBody);
        assertThat(response.getStatusCode()).isEqualTo(201);

        String responseBody = response.asString();
        registrationResponse = objectMapper.readValue(responseBody, RegistrationResponse.class);

        assertThat(registrationResponse.getMessage()).contains("Пользователь успешно зарегистрирован");
        assertThat(registrationResponse.getToken()).isNotEmpty();
        assertThat(registrationResponse.getUser().getId()).isGreaterThan(0);
        assertThat(registrationResponse.getUser().getUsername()).isEqualTo("AndreyBlinov1");
        assertThat(registrationResponse.getUser().getEmail()).isEqualTo("andrey.blinov1@mail.ru");
        assertThat(registrationResponse.getUser().getRole()).isEqualTo("USER");
        assertThat(registrationResponse.getUser().getFirst_name()).isEqualTo("Андрей");
        assertThat(registrationResponse.getUser().getLast_name()).isEqualTo("Блинов");
        assertThat(registrationResponse.getUser().getPhone()).isEqualTo("+7 (999) 100-10-11");
        assertThat(registrationResponse.getUser().getIs_active()).isEqualTo(true);
    }

    @Step("Отправка DELETE-запроса на /users на удаление пользователя")
    @AfterEach
    public void tearDown() {
        Response response = apiClient.deleteUser(registrationResponse.getUser().getId());
        assertThat(response.getStatusCode()).isEqualTo(204);
    }
}
