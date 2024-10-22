package org.example;

import org.example.service.WebService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static Map<String, String> cache = new HashMap<>();
    private static String originServer;

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        WebService webService = WebService.getInstance();
        var command = sc.nextLine();
        String regex = "--port\\s+(\\d+)\\s+--origin\\s+(\\S+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        int port;

        if (matcher.find()) {
            port = Integer.parseInt(matcher.group(1));
            String origin = matcher.group(2);
            System.out.println("Port: " + port);
            System.out.println("Origin: " + origin);

            webService.setOriginServer(origin);

            webService.startServer(port);
        } else {
            System.out.println("Invalid input. Please use the format: --port <number> --origin <url>");
        }
    }
}
