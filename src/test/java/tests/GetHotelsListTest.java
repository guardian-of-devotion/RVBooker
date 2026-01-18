package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.HotelsList;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetHotelsListTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetHotelsList() throws JsonProcessingException {
        Response response = apiClient.getHotelsList();
        String responseBody = response.getBody().asString();
        List<HotelsList> hotelsList = objectMapper.readValue(responseBody, new TypeReference<List<HotelsList>>() {
        });
        assertThat(hotelsList).isNotEmpty();
        assertThat(response.getStatusCode()).isEqualTo(200);
    }
}
