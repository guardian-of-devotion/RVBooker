package core.clients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import core.settings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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
                .header("Accept", "application/json");
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
                .post(ApiEndpoints.AUTH.getPath())
                .then()
                .extract().response();
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
}
