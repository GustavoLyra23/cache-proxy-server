package org.example.service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebService {

    private static final WebService instance = new WebService();

    private WebService() {
    }

    public static WebService getInstance() {
        return instance;
    }

    public String sendRequest(String url) {
        return "Response from " + url;

    }

    public void startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/");
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at port " + port);
    }


}
