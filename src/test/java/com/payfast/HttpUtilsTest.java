package com.payfast;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class HttpUtilsTest {

    @Test
    void testSendHttpGetRequest() {
        try {
            String response = HttpUtils.sendHttpGetRequest("https://jsonplaceholder.typicode.com/posts/1");
            assertNotNull(response);
            assertTrue(response.contains("userId"));
            assertTrue(response.contains("id"));
            assertTrue(response.contains("title"));
            assertTrue(response.contains("body"));
        } catch (IOException | URISyntaxException | InterruptedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testSendHttpGetRequestWithInvalidUrl() {
        assertThrows(IOException.class, () -> {
            HttpUtils.sendHttpGetRequest("https://jsonplaceholder.typicode.com/nonexistent");
        });
    }

    @Test
    void testSendHttpPostRequest() {
        try {
            String requestBody = "{\"title\":\"foo\",\"body\":\"bar\",\"userId\":1}";
            String response = HttpUtils.sendHttpPostRequest("https://jsonplaceholder.typicode.com/posts", requestBody);
            assertNotNull(response);
            assertTrue(response.contains("title"));
            assertTrue(response.contains("body"));
            assertTrue(response.contains("userId"));
            assertTrue(response.contains("id"));
        } catch (IOException | URISyntaxException | InterruptedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testSendHttpPostRequestWithInvalidUrl() {
        assertThrows(IOException.class, () -> {
            String requestBody = "{\"title\":\"foo\",\"body\":\"bar\",\"userId\":1}";
            HttpUtils.sendHttpPostRequest("https://jsonplaceholder.typicode.com/nonexistent", requestBody);
        });
    }
}
