package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.CreateUser;
import core.models.CreatedUser;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteUserTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateUser createUser;
    private CreatedUser createdUser;

    @BeforeEach
    public void setup() throws JsonProcessingException {
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

        String requestBody = objectMapper.writeValueAsString(createUser);
        Response response = apiClient.createUser(requestBody);

        assertThat(response.getStatusCode()).isEqualTo(201);

        String responseBody = response.asString();
        createdUser = objectMapper.readValue(responseBody, CreatedUser.class);
    }

    @Test
    public void testDeleteUser() throws Exception {
        Response response = apiClient.deleteUser(createdUser.getId());
        assertThat(response.statusCode()).isEqualTo(204);

        Response secondResponse = apiClient.getCurrentUser(createdUser.getId());
        assertThat(secondResponse.statusCode()).isEqualTo(404);
    }
}
