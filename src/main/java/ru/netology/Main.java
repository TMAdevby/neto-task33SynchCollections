package ru.netology;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;


public class Main {
    public static ArrayBlockingQueue aQueue = new ArrayBlockingQueue(100);
    public static ArrayBlockingQueue bQueue = new ArrayBlockingQueue(100);
    public static ArrayBlockingQueue cQueue = new ArrayBlockingQueue(100);

    public static void main(String[] args) {

        Thread generateThread = new Thread(() -> {
            System.out.println("generateThread стартовал");
            String text = "";
            for (int i = 0; i < 10_000; i++) {
                text = generateText("abc", 100_000);
                try {
                    aQueue.put(text);
                    System.out.println("putA");
                    bQueue.put(text);
                    System.out.println("putB");
                    cQueue.put(text);
                    System.out.println("putC");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        generateThread.start();

        Thread countAthread = new Thread(() -> {
            System.out.println("countAthread стартовал");
            String text = "";
            String textInMemory = "";
            int aMax = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    text = (String) aQueue.take();
                    System.out.println("takeA");
                } catch (InterruptedException e) {
                    return;
                }
                int aCount = 0;
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == 'a') {
                        aCount++;
                    }
                }
                if (aMax < aCount) {
                    aMax = aCount;
                    textInMemory = text;
                }
            }
            String out = textInMemory.substring(0, 100);
            System.out.println("'a' max -> " + out + "\n встречается " + aMax + " раз");
        });

        Thread countBthread = new Thread(() -> {
            System.out.println("countBthread стартовал");
            String text = "";
            String textInMemory = "";
            int bMax = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    text = (String) bQueue.take();
                    System.out.println("takeB");
                } catch (InterruptedException e) {
                    return;
                }
                int bCount = 0;
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == 'b') {
                        bCount++;
                    }
                }
                if (bMax < bCount) {
                    bMax = bCount;
                    textInMemory = text;
                }
            }
            String out = textInMemory.substring(0, 100);
            System.out.println("'b' max -> " + out + "\n встречается " + bMax + " раз");
        });

        Thread countCthread = new Thread(() -> {
            System.out.println("countCthread стартовал");
            String text = "";
            String textInMemory = "";
            int cMax = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    text = (String) cQueue.take();
                    System.out.println("takeC");
                } catch (InterruptedException e) {
                    return;
                }
                int aCount = 0;
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == 'c') {
                        aCount++;
                    }
                }
                if (cMax < aCount) {
                    cMax = aCount;
                    textInMemory = text;
                }
            }
            String out = textInMemory.substring(0, 100);
            System.out.println("'c' max -> " + out + "\n встречается " + cMax + " раз");
        });

        countAthread.start();
        countBthread.start();
        countCthread.start();

        try {
            generateThread.join();
            countAthread.join();
            countBthread.join();
            countCthread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Программа завершена.");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
