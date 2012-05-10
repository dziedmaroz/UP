package com.exadel.education.networking.nio;

public class MyProtocolTest {
    public static void main(String[] args) {
        doTestMessage("");
        doTestMessage("12345");
        doTestMessage("ab#c##d###");

    }

    private static void doTestMessage(String message) {
        System.out.println("--------------------- Test message: " + message);
        String encoded = MyProtocol.encodeMessage(message);
        System.out.println("Encode[" + message + "]=" + encoded);

        boolean canDecode = MyProtocol.canDecode(encoded);
        System.out.println("Can decode = " + canDecode);

        if (canDecode) {
            String decoded = MyProtocol.decodeMessage(encoded);
            System.out.println("Decoded = " + decoded);
            System.out.println("message.equals(decoded)=" + message.equals(decoded));
        }
    }
}