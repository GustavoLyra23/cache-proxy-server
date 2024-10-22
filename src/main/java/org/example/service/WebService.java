package org.example.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class WebService implements HttpHandler {

    private static final WebService instance = new WebService();
    private static final HashMap<String, String> cache = new HashMap<>();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static String originServer;

    private WebService() {
    }

    public static WebService getInstance() {
        return instance;
    }

    public void setOriginServer(String origin) {
        originServer = origin;
    }

    public HttpServer startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", this);
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at port " + port);
        return server;
    }

    public void stopServer(HttpServer server) {
        server.stop(0);
        System.out.println("Server stopped");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestUrl = exchange.getRequestURI().toString();
        String cacheKey = originServer + requestUrl;


        if (cache.containsKey(cacheKey)) {
            sendCachedResponse(exchange, cache.get(cacheKey));
        } else {
            forwardRequestToOrigin(exchange, cacheKey);
        }
    }

    private void forwardRequestToOrigin(HttpExchange exchange, String requestPath) throws IOException {
        try {

            System.out.println("Forwarding request to: " + requestPath);

            URI uri = URI.create(requestPath);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            cache.put(requestPath, response.body());

            exchange.getResponseHeaders().add("X-Cache", "MISS");
            exchange.sendResponseHeaders(200, response.body().length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.body().getBytes());
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
        }
    }


    private void sendCachedResponse(HttpExchange exchange, String cachedResponse) throws IOException {
        exchange.getResponseHeaders().add("X-Cache", "HIT");
        exchange.sendResponseHeaders(200, cachedResponse.length());
        OutputStream os = exchange.getResponseBody();
        os.write(cachedResponse.getBytes());
        os.close();
    }
}
