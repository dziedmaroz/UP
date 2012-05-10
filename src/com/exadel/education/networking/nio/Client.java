package com.exadel.education.networking.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Simple client that sends several messages to server
 */
public class Client {
    private static String readText(InputStream stream) throws IOException {
        InputStreamReader in = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(in);

        StringBuilder builder = new StringBuilder();
        String text;
        do {
            text = reader.readLine();
            builder.append(text);
        }
        while (!text.isEmpty());

        return builder.toString();
    }

    private void testClient(String localhost, int port) throws IOException {
        try (Socket socket = new Socket(localhost, port)) {
            //socket.getOutputStream().write("dsfsdfsdfsdfsdf".getBytes());
            // Thread.sleep(10);
            int number = (int) (Math.random() * 100);
            String request = "Test message #" + number;
            socket.getOutputStream().write(MyProtocol.encodeMessage(request).getBytes());

            String response = readText(socket.getInputStream());
            System.out.println("response = " + response);
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.testClient("localhost", 9090);
    }
}
