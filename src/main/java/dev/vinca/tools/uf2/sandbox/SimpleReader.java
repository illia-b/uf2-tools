package dev.vinca.tools.uf2.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class SimpleReader {

    private static String uf2Path = "blink2.uf2";
    private static String binPath = "blink2.bin";

    public static void main(String[] args) throws IOException {
        printUF2(uf2Path);
    }

    private static void printUF2(String path) throws IOException {
        var bufferIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        ByteBuffer bytes = ByteBuffer.allocate(512);
        bytes.order(ByteOrder.LITTLE_ENDIAN);

        int blocks = 0;
        while (bufferIS.read(bytes.array(), 0, 512) > 0) {
//        bufferIS.read(bytes.array(), 0, 512);
            System.out.println("Block " + (blocks++));
            printHex(bytes);

            System.out.println();
        }
    }

    private static void printHex(ByteBuffer bytes) {
        IntBuffer ints = bytes.asIntBuffer();
        if (ints.get(0x00) != 0x0a324655) throw new IllegalArgumentException("first magic incorrect");
        if (ints.get(0x01) != 0x9e5d5157) throw new IllegalArgumentException("second magic incorrect");
        if (ints.get(0x7f) != 0x0ab16f30) throw new IllegalArgumentException("last magic incorrect");

        System.out.println(String.format("Flags:    %08X", ints.get(0x02)));
        System.out.println(String.format("Target:   %08X", ints.get(0x03)));
        System.out.println(String.format("Size:     %08X (%d)", ints.get(0x04), ints.get(0x04)));
        System.out.println(String.format("Block:    %03d of %03d", ints.get(0x05), ints.get(0x06)));
        System.out.println(String.format("Fam/Size: %08X", ints.get(0x07)));

        for (int i = 0, size = ints.get(0x04); i < size; i++) {
            System.out.print(String.format("%02X ", bytes.get(i + 32)));
            if (i % 8 == 7) System.out.print("|");
            if (i % 32 == 31) System.out.println();
        }
    }
}
