package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.service.WebService;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        WebService webService = WebService.getInstance();
        String regex = "--port\\s+(\\d+)\\s+--origin\\s+(\\S+)";

        do {
            var command = sc.nextLine();
            checkCommand(command, regex, webService);
        } while (true);
    }

    private static void checkCommand(String command, String regex, WebService webService) throws IOException {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            var port = Integer.parseInt(matcher.group(1));
            String origin = matcher.group(2);
            System.out.println("Port: " + port);
            System.out.println("Origin: " + origin);
            webService.setOriginServer(origin);
            webService.startServer(port);
        } else if (command.equalsIgnoreCase("exit")) {
            System.exit(0);
        } else {
            System.out.println("Invalid command");
        }
    }



}
