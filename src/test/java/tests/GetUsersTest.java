package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.UserResponse;
import core.models.UsersList;
import io.qameta.allure.*;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@ExtendWith(AllureJunit5.class)
@Epic("Работа с данными пользователя")
@Feature("Получение списка пользователей")
public class GetUsersTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private UserResponse userResponse;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        userResponse = new UserResponse();
    }

    @Step("Отправка GET-запроса на /users на получение списка пользователей")
    @Test
    public void testGetUsers() throws JsonProcessingException {
        Response response = apiClient.getUsers();
        assertThat(response.getStatusCode()).isEqualTo(200);
        String responseBody = response.getBody().asString();
        List<UsersList> usersList = objectMapper.readValue(responseBody, new TypeReference<List<UsersList>>() {
        });

        assertThat(usersList).isNotEmpty();

        for (UsersList user: usersList) {
            assertThat(user.getId()).isGreaterThan(0);
            assertThat(user.getUsername()).isNotEmpty();
            assertThat(user.getEmail()).isNotEmpty();
            assertThat(user.getFirstName()).isNotEmpty();
            assertThat(user.getLastName()).isNotEmpty();
            assertThat(user.getRole()).isNotEmpty();
            if (user.getPhone() != null) {
                assertThat(user.getPhone()).isNotEmpty();
            }
            assertThat(user.isActive()).isTrue();
            assertThat(user.getCreatedAt()).isNotEmpty();
            assertThat(user.getUpdatedAt()).isNotEmpty();
        }
    }

    @Step("Отправка GET-запроса на /users на получение пользователя по ID")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testGetUsersById() throws JsonProcessingException {
        Response response = apiClient.getCurrentUser(2091);
        String responseBody = response.asString();

        userResponse = objectMapper.readValue(responseBody, UserResponse.class);

        assertThat(userResponse.getId()).isGreaterThan(0);
        assertThat(userResponse.getUsername()).isEqualTo("Abba123");
        assertThat(userResponse.getEmail()).isEqualTo("abba@mail.ru");
        assertThat(userResponse.getFirst_name()).isEqualTo("Abbas");
        assertThat(userResponse.getLast_name()).isEqualTo("Babbas");
        assertThat(userResponse.getRole()).isEqualTo("USER");
    }
}
