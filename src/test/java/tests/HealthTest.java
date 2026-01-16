package tests;

import core.clients.APIClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthTest {
    private APIClient apiClient;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
    }

    @Test
    public void testHealth() {
        Response response = apiClient.checkHeath();
        assertThat(response.getStatusCode()).isEqualTo(200);

    }
}
