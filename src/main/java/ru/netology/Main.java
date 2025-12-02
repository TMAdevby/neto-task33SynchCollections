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

        Thread generateThread = new Thread(() ->{
            String text = "";
            for (int i = 0; i < 10_000; i++) {
                text = generateText("abc", 100_000);
                try {
                    aQueue.put(text);
                    System.out.println("put");
                    bQueue.put(text);
                    cQueue.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        generateThread.start();

        Thread countAthread = new Thread(() -> {
            String text = "";
            String textInMemory = "";
            int aMax = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    text = (String) aQueue.take();
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
            System.out.println("'a' max -> " + textInMemory + "\n встречается " + aMax + " раз");
        });

        countAthread.start();
        try {
            generateThread.join();
            countAthread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Программа завершена.");


    }





    public static String generateText (String letters,int length){
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
