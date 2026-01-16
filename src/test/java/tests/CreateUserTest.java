package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.CreateUser;
import core.models.CreatedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CreateUserTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateUser createUser;
    private CreatedUser createdUser;

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

    @AfterEach
    public void tearDown() {
        apiClient.deleteUser(createdUser.getId());
        assertThat(apiClient.getCurrentUser(createdUser.getId()).getStatusCode()).isEqualTo(404);

    }
}
