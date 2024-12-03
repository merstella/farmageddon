package io.github.farmageddon;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationLogger;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileApplicationLogger implements ApplicationLogger {
    private PrintWriter writer;

    public FileApplicationLogger(String filePath) {
        try {
            writer = new PrintWriter(new FileWriter(filePath, true)); // Append mode
        } catch (IOException e) {
            System.err.println("Failed to initialize file logger: " + e.getMessage());
        }
    }

    @Override
    public void log(String tag, String message) {
        if (writer != null) {
            writer.println("LOG: [" + tag + "] " + message);
            writer.flush();
        }
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        if (writer != null) {
            writer.println("LOG: [" + tag + "] " + message);
            exception.printStackTrace(writer);
            writer.flush();
        }
    }

    @Override
    public void error(String tag, String message) {
        if (writer != null) {
            writer.println("ERROR: [" + tag + "] " + message);
            writer.flush();
        }
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        if (writer != null) {
            writer.println("ERROR: [" + tag + "] " + message);
            exception.printStackTrace(writer);
            writer.flush();
        }
    }

    @Override
    public void debug(String tag, String message) {
        if (writer != null) {
            writer.println("DEBUG: [" + tag + "] " + message);
            writer.flush();
        }
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        if (writer != null) {
            writer.println("DEBUG: [" + tag + "] " + message);
            exception.printStackTrace(writer);
            writer.flush();
        }
    }

    public void dispose() {
        if (writer != null) {
            writer.close();
        }
    }
}
