package com.payfast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

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

    public static String sendHttpPostRequest(String url, Map<String, String> requestBody)
            throws IOException, URISyntaxException, InterruptedException {
        StringBuilder requestBodyString = new StringBuilder();
        for (Map.Entry<String, String> entry : requestBody.entrySet()) {
            requestBodyString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (requestBodyString.length() > 0) {
            requestBodyString.deleteCharAt(requestBodyString.length() - 1);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("content-type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyString.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return response.body();
        } else {
            throw new IOException("HTTP POST request failed with status code: " + response.statusCode());
        }
    }

    /**
     * Check if the given IP address is a local IP address
     *
     * @param ipAddress IP address to check
     * @return true if the IP address is local, false otherwise
     */
    public static boolean isLocalIpAddress(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            return address.isLoopbackAddress() || NetworkInterface.getByInetAddress(address) != null;
        } catch (UnknownHostException | SocketException e) {
            // Handle exceptions as needed
            e.printStackTrace();
            return false;
        }
    }

    public static String convertMapToQueryString(Map<String, String> data) {
        StringBuilder query = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (first) {
                query.append("?");
                first = false;
            } else {
                query.append("&");
            }
            query.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return query.toString();
    }
}

