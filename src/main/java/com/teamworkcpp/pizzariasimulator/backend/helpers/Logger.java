package com.teamworkcpp.pizzariasimulator.backend.helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_FILE_PATH = "log.txt";
    private static final Object lock = new Object();

    public static synchronized void log(String data) throws IOException {
        synchronized (lock) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))))
            {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                System.out.println("[" + timestamp + "] " + data);
                writer.println("[" + timestamp + "] " + data);
            }
        }
    }
}