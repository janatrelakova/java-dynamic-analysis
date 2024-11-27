package cz.muni.fi.xtrelak.injector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    private static BufferedWriter writer;
    private static final Logger logger = LogManager.getLogger(Writer.class);


    public static void setupFileWriter() {
        try {
            File logFile = new File("invoked-calls.txt");
            if (!logFile.exists()) {
                var created = logFile.createNewFile();
                if (!created) {
                    throw new IOException("Failed to create the dynamic analysis file");
                }
                logger.info("dynamic analysis file was created");
            } else {
                logger.info("dynamic analysis file already exists");
            }
            writer = new BufferedWriter(new FileWriter(logFile, true));
        } catch (IOException e) {
            logger.error("Failed to create the dynamic analysis file", e);
        }

        // Ensure the writer is closed when the JVM shuts down
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                logger.error("Failed to close the writer for dynamic analysis results", e);
            }
        }));
    }

    public static void writeToFile(String message) {
        synchronized (Injector.class) {
            try {
                if (writer != null) {
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                }
            } catch (IOException e) {
                logger.error("Failed to write to the dynamic analysis file", e);
            }
        }
    }
}
