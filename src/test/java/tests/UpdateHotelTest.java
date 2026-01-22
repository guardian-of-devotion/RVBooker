package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.CreateHotel;
import core.models.CreatedHotel;
import core.models.UpdateHotel;
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
@Feature("Обновление информации об отеле")
public class UpdateHotelTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateHotel createHotel;
    private CreatedHotel createdHotel;
    private Integer hotelId;

    @Step("Подготовка данных отеля и отправка POST-запроса на /hotels на создание")
    @BeforeEach
    public void setup() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();

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

        hotelId = createdHotel.getId();
    }

    @Feature("Отправка PUT-запроса на /hotels на обновление информации об отеле")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testUpdateHotel() throws JsonProcessingException {
        UpdateHotel updateHotel = new UpdateHotel();
        updateHotel.setName("Luuux");
        updateHotel.setDescription("String");
        updateHotel.setAddress("Moscow");
        updateHotel.setCity("Moscow");
        updateHotel.setCountry("Russia");
        updateHotel.setPostalCode("100100");
        updateHotel.setLatitude("3");
        updateHotel.setLongitude("5");
        updateHotel.setPhone("+7995202020");
        updateHotel.setEmail("luuux@ya.ru");
        updateHotel.setWebsite("https://luuux.ru");
        updateHotel.setStarRating(5);
        updateHotel.setActive(true);

        String requestBody = objectMapper.writeValueAsString(updateHotel);
        Response response = apiClient.updateHotel(hotelId, requestBody);
        assertThat(response.getStatusCode()).isEqualTo(200);

        UpdateHotel responseHotel = objectMapper.readValue(response.asString(), UpdateHotel.class);

        assertThat(responseHotel.getName()).isEqualTo("Luuux");
        assertThat(responseHotel.getDescription()).isEqualTo("String");
        assertThat(responseHotel.getAddress()).isEqualTo("Moscow");
        assertThat(responseHotel.getCity()).isEqualTo("Moscow");

        /// И так далее

    }

    @Step("Отправка DELETE-запроса на /hotels на удаление отеля")
    @AfterEach
    public void tearDown() {
        Response response = apiClient.deleteHotel(hotelId);
        assertThat(response.getStatusCode()).isEqualTo(204);
    }
}
