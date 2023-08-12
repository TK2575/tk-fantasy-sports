package dev.tk2575.fantasysports.details.filewriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public interface FileWriterDetail {
    default void writeToFile(String fileName, String delimiter) throws IOException {
        try (var writer = new PrintWriter(new java.io.FileWriter(fileName))) {
            List<String> rows = getDelimitedRows(delimiter);
            writer.write(String.join("\n", rows));
        }
    }
    
    //TODO move to default
    List<String> getDelimitedRows(CharSequence delimiter);
    
    String[] getHeaders();
}
