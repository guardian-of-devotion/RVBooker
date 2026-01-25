package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.CreateUser;
import core.models.CreatedUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AllureJunit5.class)
@Epic("Работа с данными пользователя")
@Feature("Создание нового пользователя")
public class CreateUserTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateUser createUser;
    private CreatedUser createdUser;

    @Step("Подготовка данных пользователя")
    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        createUser = new CreateUser();
        createdUser = new CreatedUser();

        createUser.setUsername("Abba12345");
        createUser.setEmail("abba12@mail.ru");
        createUser.setFirstname("Abbasas");
        createUser.setLastname("Babbasas");
        createUser.setPassword("USER123");
        createUser.setRole("USER");
    }

    @Step("Отправка POST-запроса на /users для создания нового пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testCreateUser() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(createUser);
        Response response = apiClient.createUser(requestBody);

        assertThat(response.getStatusCode()).isEqualTo(201);

        String responseBody = response.asString();
        createdUser = objectMapper.readValue(responseBody, CreatedUser.class);

        assertEquals(createdUser.getUsername(), createUser.getUsername());
        assertEquals(createdUser.getEmail(), createUser.getEmail());
        assertEquals(createdUser.getFirstName(), createUser.getFirstname());
        assertEquals(createdUser.getLastName(), createUser.getLastname());
        assertEquals(createdUser.getRole(), createUser.getRole());
    }

    @Step("Отправка DELETE-запроса на /users для удаления пользователя")
    @AfterEach
    public void tearDown() {
        apiClient.deleteUser(createdUser.getId());
        assertThat(apiClient.getCurrentUser(createdUser.getId()).getStatusCode()).isEqualTo(404);

    }
}
