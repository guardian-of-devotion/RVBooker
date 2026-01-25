package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.CreateHotel;
import core.models.CreatedHotel;
import core.models.HotelsList;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(AllureJunit5.class)
@Epic("Работа с данными отеля")
@Feature("Получение конкретного отеля путем использования фильтрации")
public class GetHotelWithFiltersTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateHotel createHotel;
    private CreatedHotel createdHotel;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        createHotel = new CreateHotel();
        createdHotel = new CreatedHotel();

        createHotel.setName("Radisson");
        createHotel.setDescription("String");
        createHotel.setAddress("Kazan");
        createHotel.setCity("Kazan");
        createHotel.setCountry("Russia");
        createHotel.setPostalCode("123456");
        createHotel.setLatitude("0");
        createHotel.setLongitude("0");
        createHotel.setPhone("+1234567890");
        createHotel.setEmail("Radisson@ya.ru");
        createHotel.setWebsite("https://radisson.ru");
        createHotel.setStarRating(5);
        createHotel.setActive(true);

        String requestBody = objectMapper.writeValueAsString(createHotel);
        Response response = apiClient.createNewHotel(requestBody);

        String responseBody = response.asString();
        createdHotel = objectMapper.readValue(responseBody, CreatedHotel.class);
    }

    @Severity(SeverityLevel.CRITICAL)
    @ParameterizedTest
    @ArgumentsSource(tests.providers.HotelsFilterProvider.class)
    public void testGetHotelWithFilters(String name, String address, String city, String country) throws JsonProcessingException {
        Response response = apiClient.getHotelWithFilters(name, address, city, country);

        assertThat(response.getStatusCode()).isEqualTo(200);
        String responseBody = response.getBody().asString();

        List<HotelsList> hotelsList = objectMapper.readValue(responseBody, new TypeReference<List<HotelsList>>() {
        });

        assertThat(hotelsList).isNotEmpty();

        for (HotelsList hotel: hotelsList) {
            if (name != null) {
                assertThat(hotel.getName()).isEqualTo(name);
            }
            if (address != null) {
                assertThat(hotel.getAddress()).isEqualTo(address);
            }
            if (city != null) {
                assertThat(hotel.getCity()).isEqualTo(city);
            }
            if (country != null) {
                assertThat(hotel.getCountry()).isEqualTo(country);
            }
        }
    }

    @AfterEach
    public void tearDown() {
        apiClient.deleteHotel(createdHotel.getId());
    }
}
