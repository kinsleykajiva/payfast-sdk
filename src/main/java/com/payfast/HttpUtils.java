package com.payfast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtils {

    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    /**
     * Sends an HTTP GET request to the specified URL and returns the response body as a string.
     *
     * @param url The URL to send the GET request to.
     * @return The response body as a string.
     * @throws IOException if an I/O error occurs.
     * @throws URISyntaxException if the URL is malformed.
     * @throws InterruptedException if the request is interrupted.
     */
    public static String sendHttpGetRequest(String url) throws IOException, URISyntaxException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return response.body();
        } else {
            throw new IOException("HTTP GET request failed with status code: " + response.statusCode());
        }
    }

    /**
     * Sends an HTTP POST request to the specified URL with the provided request body and returns the response body as a string.
     *
     * @param url The URL to send the POST request to.
     * @param requestBody The request body to send in the POST request.
     * @return The response body as a string.
     * @throws IOException if an I/O error occurs.
     * @throws URISyntaxException if the URL is malformed.
     * @throws InterruptedException if the request is interrupted.
     */
    public static String sendHttpPostRequest(String url, String requestBody) throws IOException, URISyntaxException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return response.body();
        } else {
            throw new IOException("HTTP POST request failed with status code: " + response.statusCode());
        }
    }
}

