package com.khanghoang.client.network;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    public static InputStream get(String endpoint) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        int code = conn.getResponseCode();
        if (code == 200) {
            return conn.getInputStream();
        } else {
            throw new RuntimeException("HTTP GET failed. Code: " + code);
        }
    }

    public static InputStream post(String endpoint, String jsonBody) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes());
        }

        int code = conn.getResponseCode();
        if (code == 200 || code == 201) {
            return conn.getInputStream();
        } else {
            throw new RuntimeException("HTTP POST failed. Code: " + code);
        }
    }

}
