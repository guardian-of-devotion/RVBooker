package core.clients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import core.settings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;

public class APIClient {
    private final String baseUrl;
    private String token;

    public APIClient() {
        this.baseUrl = determineBaseUrl();
    }

    private String determineBaseUrl() {
        String environment = System.getProperty("env", "test");
        String configFileName = "application-" + environment + ".properties";

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalStateException("Configuration file not found: " + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load configuration file: " + configFileName, e);
        }

        return properties.getProperty("baseUrl");
    }

    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .filter(addAuthTokenFilter());
    }

    public Response checkHeath() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.HEALTH.getPath())
                .then()
                .extract()
                .response();
    }

    public Response authorize(String requestBody) {
        return getRequestSpec()
                .body(requestBody)
                .when()
                .post(ApiEndpoints.AUTH.getPath() + "/login")
                .then()
                .extract().response();
    }

    public void createToken(String email, String password) {
        String requestBody = String.format("{ \"email\": \"%s\", \"password\": \"%s\" }", email, password);

        Response response = getRequestSpec()
                .body(requestBody)
                .when()
                .post(ApiEndpoints.AUTH.getPath() + "/login")
                .then()
                .extract()
                .response();

        token = response.jsonPath().getString("token");
    }

    public String getToken() {
        return token;
    }

    private Filter addAuthTokenFilter() {
        return (FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) -> {
            if (token != null) {
                requestSpec.header("Authorization", "Bearer " + token);
            }
                return ctx.next(requestSpec, responseSpec);
        };
    }


    public Response register(String requestBody) {
        return getRequestSpec()
                .body(requestBody)
                .when()
                .post(ApiEndpoints.REGISTER.getPath())
                .then()
                .log().all()
                .extract().response();
    }

    public Response deleteUser(int userId) {
        return getRequestSpec()
                .pathParam("id", userId)
                .when()
                .delete(ApiEndpoints.USERS.getPath() + "/{id}")
                .then()
                .log().all()
                .extract().response();
    }

    public Response getUsers() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.USERS.getPath())
                .then()
                .extract()
                .response();
    }

    public Response getCurrentUser(int userId) {
        return getRequestSpec()
                .pathParam("id", userId)
                .when()
                .get(ApiEndpoints.USERS.getPath() + "/{id}")
                .then()
                .extract()
                .response();
    }

    public Response createUser(String createUser) {
        return getRequestSpec()
                .body(createUser)
                .when()
                .post(ApiEndpoints.USERS.getPath())
                .then()
                .extract()
                .response();
    }

    public Response updateUser(int userId, String requestBody) {
        return getRequestSpec()
                .pathParam("id", userId)
                .body(requestBody)
                .when()
                .put(ApiEndpoints.USERS.getPath() + "/{id}")
                .then()
                .extract()
                .response();
    }

    public Response getHotelsList() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.HOTELS.getPath())
                .then()
                .extract()
                .response();
    }

    public Response getHotelById(int hotelId) {
        return getRequestSpec()
                .pathParam("id", hotelId)
                .when()
                .get(ApiEndpoints.HOTELS.getPath() + "/{id}")
                .then()
                .extract()
                .response();
    }

    public Response getHotelWithFilters(String name, String address, String city, String country) {
        RequestSpecification spec = getRequestSpec();

        if (name != null) {
            spec = spec.queryParam("name", name);
        }

        if (address != null) {
            spec = spec.queryParam("address", address);
        }

        if (city != null) {
            spec = spec.queryParam("city", city);
        }

        if (country != null) {
            spec = spec.queryParam("country", country);
        }

        return spec
                .when()
                .get(ApiEndpoints.HOTELS.getPath())
                .then()
                .extract()
                .response();
    }

    public Response createNewHotel(String createHotel) {
        return getRequestSpec()
                .body(createHotel)
                .log().all()
                .when()
                .post(ApiEndpoints.HOTELS.getPath())
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response deleteHotel(int hotelId) {
        return getRequestSpec()
                .pathParam("id", hotelId)
                .when()
                .delete(ApiEndpoints.HOTELS.getPath() + "/{id}")
                .then()
                .extract()
                .response();
    }

    public Response updateHotel(int hotelId, String requestBody) {
        return getRequestSpec()
                .pathParam("id", hotelId)
                .body(requestBody)
                .when()
                .put(ApiEndpoints.HOTELS.getPath() + "/{id}")
                .then()
                .extract()
                .response();
    }

    public Response getCountOfUnreadMessages() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.SUPPORT.getPath() + "/unread-count")
                .then()
                .extract()
                .response();
    }
}
