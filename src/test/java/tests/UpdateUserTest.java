package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.UpdateUser;
import core.models.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateUserTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private UpdateUser updateUser;
    private UserResponse userResponse;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        updateUser = new UpdateUser();

        updateUser.setUsername("igor.voronov@protnie.ru");
        updateUser.setEmail("abiba@mail.ru");
        updateUser.setFirstname("Abiba");
        updateUser.setLastname("Bibaba");
        updateUser.setPhone("+7 (999) 102-01-01");
        updateUser.setRole("USER");
        updateUser.setPassword("abiba123");
    }

    @Test
    public void testUpdateUser() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(updateUser);
        Response response = apiClient.updateUser(2081, requestBody);

        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.asString();
        userResponse = objectMapper.readValue(responseBody, UserResponse.class);

        assertEquals(userResponse.getUsername(), updateUser.getUsername());
        assertEquals(userResponse.getEmail(), updateUser.getEmail());
        assertEquals(userResponse.getFirst_name(), updateUser.getFirstname());
        assertEquals(userResponse.getLast_name(), updateUser.getLastname());
        assertEquals(userResponse.getPhone(), updateUser.getPhone());
        assertEquals(userResponse.getRole(), updateUser.getRole());

    }
}
