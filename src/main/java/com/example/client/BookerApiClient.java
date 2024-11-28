package com.example.client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BookerApiClient {
    private static final String BASE_URL = "https://restful-booker.herokuapp.com";

    public Response createToken(String username, String password) {
        String payload = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(BASE_URL + "/auth");
    }

    public Response getAllBookings() {
        return given()
                .get(BASE_URL + "/booking");
    }

    public Response getBooking(int id) {
        return given()
                .accept(ContentType.JSON)
                .get(BASE_URL + "/booking/" + id);
    }

    public Response createBooking(String bookingJson) {
        return given()
                .contentType(ContentType.JSON)
                .body(bookingJson)
                .post(BASE_URL + "/booking");
    }

    public Response updateBooking(int id, String bookingJson, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(bookingJson)
                .put(BASE_URL + "/booking/" + id);
    }

    public Response deleteBooking(int id, String token) {
        return given()
                .header("Cookie", "token=" + token)
                .delete(BASE_URL + "/booking/" + id);
    }
}