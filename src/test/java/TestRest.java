
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONObject;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestRest {
    String accessToken;
    Integer id;
    String username;
    String password;
    String email;
    String name;
    String surname;

    @SneakyThrows
    @Test
    public void getGuestToken() {

        JSONObject requestBody = new JSONObject();
        requestBody.put("grant_type", "client_credentials");
        requestBody.put("scope", "guest:default");

        JsonPath response = RestAssured.given()
                .header("Authorization", "Basic ZnJvbnRfMmQ2YjBhODM5MTc0MmY1ZDc4OWQ3ZDkxNTc1NWUwOWUgOg==")
                .header("Content-Type", "application/json")
                .baseUri("http://test-api.d6.dev.devcaz.com")
                .body(requestBody.toString())
                .when()
                .post("/v2/oauth2/token")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        this.accessToken = response.getString("access_token");

        Assertions.assertNotEquals(null, accessToken);
    }

    @SneakyThrows
    @Test
    public void playerRegistration() {

        getGuestToken();

        this.password = Base64.getEncoder().encodeToString(RandomStringUtils.randomAlphanumeric(12).getBytes(StandardCharsets.UTF_8));
        this.username = RandomStringUtils.randomAlphanumeric(15);
        this.email = RandomStringUtils.randomAlphabetic(15) + "@testslotegrator.com";
        this.name = RandomStringUtils.randomAlphabetic(8);
        this.surname = RandomStringUtils.randomAlphabetic(8);

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", this.username);
        requestBody.put("password_change", this.password);
        requestBody.put("password_repeat", this.password);
        requestBody.put("email", this.email);
        requestBody.put("name", this.name);
        requestBody.put("surname", this.surname);

        JsonPath response = RestAssured.given()
                .header("Authorization", "Bearer " + this.accessToken)
                .header("Content-Type", "application/json")
                .baseUri("http://test-api.d6.dev.devcaz.com")
                .body(requestBody.toString())
                .when()
                .post("/v2/players")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .jsonPath();

        this.id = response.get("id");

        Assertions.assertNotEquals(null, this.id);
        Assertions.assertNull(response.getString("country_id"));
        Assertions.assertNull(response.getString("timezone_id"));
        Assertions.assertEquals(this.username, response.getString("username"));
        Assertions.assertEquals(requestBody.getString("email"), response.getString("email"));
        Assertions.assertEquals(requestBody.getString("name"), response.getString("name"));
        Assertions.assertEquals(requestBody.getString("surname"), response.getString("surname"));
        Assertions.assertNull(response.getString("gender"));
        Assertions.assertNull(response.getString("phone_number"));
        Assertions.assertNull(response.getString("birthdate"));
        Assertions.assertTrue(response.getBoolean("bonuses_allowed"));
        Assertions.assertFalse(response.getBoolean("is_verified"));
    }

    @SneakyThrows
    @Test
    public void authorizationByNewPlayer() {

        playerRegistration();

        JSONObject requestBody = new JSONObject();
        requestBody.put("grant_type", "password");
        requestBody.put("username", this.username);
        requestBody.put("password", this.password);

        JsonPath response = RestAssured.given()
                .header("Authorization", "Basic ZnJvbnRfMmQ2YjBhODM5MTc0MmY1ZDc4OWQ3ZDkxNTc1NWUwOWUgOg==")
                .header("Content-Type", "application/json")
                .baseUri("http://test-api.d6.dev.devcaz.com")
                .body(requestBody.toString())
                .when()
                .post("/v2/oauth2/token")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        Assertions.assertNotEquals(this.accessToken, response.getString("access_token"));
    }

    @SneakyThrows
    @Test
    public void getPlayerProfile() {

        authorizationByNewPlayer();

        JsonPath response = RestAssured.given()
                .header("Authorization", "Bearer " + this.accessToken)
                .baseUri("http://test-api.d6.dev.devcaz.com")
                .when()
                .get("/v2/players/" + this.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        Assertions.assertEquals(id, response.get("id"));
        Assertions.assertNotEquals(null, response.getString("country_id"));
        Assertions.assertNull(response.getString("timezone_id"));
        Assertions.assertEquals(username, response.getString("username"));
        Assertions.assertEquals(email, response.getString("email"));
        Assertions.assertEquals(name, response.getString("name"));
        Assertions.assertEquals(surname, response.getString("surname"));
        Assertions.assertNotEquals(null, response.getString("gender"));
        Assertions.assertNotEquals(null, response.getString("phone_number"));
        Assertions.assertNotEquals(null, response.getString("birthdate"));
        Assertions.assertTrue(response.getBoolean("bonuses_allowed"));
        Assertions.assertTrue(response.getBoolean("is_verified"));
    }

    @SneakyThrows
    @Test
    public void getNotPlayerProfile() {

        authorizationByNewPlayer();

        RestAssured.given()
                .header("Authorization", "Bearer " + this.accessToken)
                .baseUri("http://test-api.d6.dev.devcaz.com")
                .when()
                .get("/v2/players/" + this.id)
                .then()
                .assertThat()
                .statusCode(404);
    }
}
