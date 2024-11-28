package com.example;

import com.example.client.BookerApiClient;
import com.example.model.Booking;
import com.example.model.BookingDates;
import com.example.model.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookerApiTest {
    private static BookerApiClient apiClient;
    private static ObjectMapper objectMapper;
    private static String token;
    private static int bookingId;

    @BeforeAll
    static void setup() {
        apiClient = new BookerApiClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testCreateToken() throws Exception {
        Response response = apiClient.createToken("admin", "password123");
        assertThat(response.getStatusCode()).isEqualTo(200);
        
        TokenResponse tokenResponse = objectMapper.readValue(response.getBody().asString(), TokenResponse.class);
        token = tokenResponse.getToken();
        assertThat(token).isNotNull();
    }

    @Test
    @Order(2)
    void testCreateBooking() throws Exception {
        Booking booking = new Booking(
            "John",
            "Doe",
            100,
            true,
            new BookingDates("2023-01-01", "2023-01-10"),
            "Breakfast"
        );

        String bookingJson = objectMapper.writeValueAsString(booking);
        Response response = apiClient.createBooking(bookingJson);
        
        assertThat(response.getStatusCode()).isEqualTo(200);
        bookingId = response.jsonPath().getInt("bookingid");
    }

    @Test
    @Order(3)
    void testGetBooking() {
        Response response = apiClient.getBooking(bookingId);
        assertThat(response.getStatusCode()).isEqualTo(200);

        Booking booking = response.as(Booking.class);
        assertThat(booking.getFirstname()).isEqualTo("John");
        assertThat(booking.getLastname()).isEqualTo("Doe");
    }

    @Test
    @Order(4)
    void testUpdateBooking() throws Exception {
        Booking updatedBooking = new Booking(
            "Sally",
            "Brown",
            111,
            true,
            new BookingDates("2013-02-23", "2014-10-23"),
            "Breakfast"
        );

        String bookingJson = objectMapper.writeValueAsString(updatedBooking);
        Response response = apiClient.updateBooking(bookingId, bookingJson, token);
        
        assertThat(response.getStatusCode()).isEqualTo(200);
        Booking resultBooking = response.as(Booking.class);
        assertThat(resultBooking.getFirstname()).isEqualTo("Jane");
    }

    @Test
    @Order(5)
    void testDeleteBooking() {
        Response response = apiClient.deleteBooking(bookingId, token);
        assertThat(response.getStatusCode()).isEqualTo(201);
    }
}