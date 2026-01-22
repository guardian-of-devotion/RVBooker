package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.CreateHotel;
import core.models.CreatedHotel;
import core.models.HotelResponse;
import io.qameta.allure.*;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(AllureJunit5.class)
@Epic("Работа с данными отеля")
@Feature("Получение информации об отеле по ID")
public class GetHotelByIdTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateHotel createHotel;
    private CreatedHotel createdHotel;

    @Step("Подготовка данных об отеле и отправка POST-запроса на создание")
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

    @Step("Отправка GET-запроса на получение информации об отеле по ID")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void testGetHotelById() throws JsonProcessingException {
        Response response = apiClient.getHotelById(createdHotel.getId());
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();

        HotelResponse hotelResponse = objectMapper.readValue(responseBody, HotelResponse.class);

        assertThat(hotelResponse.getId()).isGreaterThan(0);
        assertThat(hotelResponse.getName()).isEqualTo("Radisson");
        assertThat(hotelResponse.getDescription()).isEqualTo("String");
        assertThat(hotelResponse.getAddress()).isEqualTo("Kazan");
        assertThat(hotelResponse.getCity()).isEqualTo("Kazan");
        assertThat(hotelResponse.getCountry()).isEqualTo("Russia");
        assertThat(hotelResponse.getPostalCode()).isEqualTo("123456");
        assertThat(hotelResponse.getLatitude()).isEqualTo("0.00000000");
        assertThat(hotelResponse.getLongitude()).isEqualTo("0.00000000");
        assertThat(hotelResponse.getPhone()).isEqualTo("+1234567890");
        assertThat(hotelResponse.getEmail()).isEqualTo("Radisson@ya.ru");
        assertThat(hotelResponse.getWebsite()).isEqualTo("https://radisson.ru");
        assertThat(hotelResponse.getStarRating()).isEqualTo(5);
        assertThat(hotelResponse.getActive()).isEqualTo(true);
        assertThat(hotelResponse.getCreatedAt()).isNotNull();
        assertThat(hotelResponse.getUpdatedAt()).isNotNull();
    }

    @Step("Отправка DELETE-запроса на удаление отеля")
    @AfterEach
    public void tearDown() {
        apiClient.deleteHotel(createdHotel.getId());
    }
}
