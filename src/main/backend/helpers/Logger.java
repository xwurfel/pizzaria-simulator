package helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_FILE_PATH = "log.txt";

    public static void log(String data) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE_PATH, true)))) {

            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.println(timestamp + " - " + data);
        }
    }
}


